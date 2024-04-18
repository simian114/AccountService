package account.service;


import account.constants.AcmeRole;
import account.constants.ChangeUserRoleErrorEnum;
import account.constants.SecurityEventEnum;
import account.domain.Role;
import account.domain.Salary;
import account.domain.User;
import account.repository.RoleRepository;
import account.repository.SalaryRepository;
import account.repository.UserRepository;
import account.security.service.SecurityEventService;
import account.web.adapter.UserAdapter;
import account.web.dto.*;
import account.web.exception.exceptions.InvalidUserException;
import account.web.exception.exceptions.UserNotFoundException;
import account.web.exception.exceptions.notFound.NotFoundException;
import account.web.mapper.SalaryMapper;
import account.web.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SalaryRepository salaryRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final SalaryMapper salaryMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityEventService securityEventService;

    public List<User> getAllUsers() {
        log.info("in get all users");
        return this.userRepository.findAll();
    }

    public UserDto registerUser(UserDto userDto, HttpServletRequest request, User currentUser) {
        log.info("Register User: {}", userDto);
        if (this.userRepository.existsByEmailIgnoreCase(userDto.getEmail())) {
            throw new InvalidUserException(InvalidUserException.USER_ALREADY_EXISTS);
        }
        User user = this.userMapper.toEntityFromUserSignupDto(userDto);
        if (this.userRepository.count() == 0) {
            log.info("There is no user in list. So this user will be ADMINISTRATOR");
            user.setRoles(List.of(this.roleRepository.findByName(AcmeRole.ADMINISTRATOR).orElseThrow(() -> new RuntimeException("There is no ADMIN Role"))));
        } else {
            log.info("There is a(more than one)user in list. So this user will be USER");
            user.setRoles(List.of(this.roleRepository.findByName(AcmeRole.USER).orElseThrow(() -> new RuntimeException("There is no USER Role"))));
        }
        User savedUser = this.userRepository.save(user);
        log.info("SUCCESS CREATE A User: {}", savedUser);
        // TODO: create event
        log.info("email: {}", userDto.getEmail());
        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.CREATE_USER,
                request,
                currentUser,
                userDto.getEmail().toLowerCase()
        );
        return this.userMapper.toDto(savedUser);
    }

    public UserChangePasswordDto changePassword(UserChangePasswordDto userChangePasswordDto,
                                                HttpServletRequest request,
                                                User currentUser
    ) {
        User user = this.userRepository.findByEmailIgnoreCase(currentUser.getEmail()).orElseThrow(() -> new InvalidUserException("user not found"));
        boolean matches = passwordEncoder.matches(userChangePasswordDto.getNewPassword(), user.getPassword());
        if (matches) {
            throw new InvalidUserException("The passwords must be different!");
        }
        user.setPassword(this.encodePassword(userChangePasswordDto.getNewPassword()));
        this.userRepository.save(user);
        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.CHANGE_PASSWORD,
                request,
                currentUser,
                user.getEmail().toLowerCase()
        );
        return this.userMapper.toChangePasswordDto(user);
    }

    public void deleteUser(String email, HttpServletRequest request, User currentUser) {
        User user = this.userRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);

        for (Role role : user.getRoles()) {
            if (role.getName().equals(AcmeRole.ADMINISTRATOR)) {
                throw new InvalidUserException("Can't remove ADMINISTRATOR role!");
            }
        }
        this.userRepository.delete(user);
        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.DELETE_USER,
                request,
                currentUser,
                email
        );
        log.info("Delete user {} success", email);
    }

    public UserDto updateUserRole(AdminUserRoleUpdateDto userRoleUpdateDto, HttpServletRequest request, User currentUser) {
        // get user or throw
        User user = this.userRepository
                .findByEmailIgnoreCase(userRoleUpdateDto.getUser())
                .orElseThrow(UserNotFoundException::new);

        Arrays.stream(AcmeRole.values()).filter(r -> r.name().equals(userRoleUpdateDto.getRole()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Role not found!"));

        Role role = this.roleRepository.findByName(AcmeRole.valueOf(userRoleUpdateDto.getRole()))
                .orElseThrow(() -> new NotFoundException("Role not found!"));

        ChangeUserRoleErrorEnum userRoleUpdateError = isGrantRole(userRoleUpdateDto.getOperation())
                ? user.addRole(role)
                : user.removeRole(role);

        if (userRoleUpdateError != null) {
            throw new InvalidUserException(userRoleUpdateError.getMsg());
        }
        User save = this.userRepository.save(user);
        //
        if (isGrantRole(userRoleUpdateDto.getOperation())) {
            securityEventService.createSecurityEvent(
                    SecurityEventEnum.GRANT_ROLE,
                    request,
                    currentUser,
                    "Grant role " + userRoleUpdateDto.getRole() + " to " + save.getEmail().toLowerCase()
            );
        } else {
            securityEventService.createSecurityEvent(
                    SecurityEventEnum.REMOVE_ROLE,
                    request,
                    currentUser,
                    "Remove role " + userRoleUpdateDto.getRole() + " from " + save.getEmail().toLowerCase()
            );
        }
        return this.userMapper.toDto(save);
    }

    public List<EmployeeSalaryDto> getAllPayments(User currentUser) {
        List<Salary> userSalaries = this.salaryRepository.findByUserIdWithUserOrderBySalaryIdPeriodDesc(currentUser.getUserId());
        return userSalaries
                .stream()
                .map(this.salaryMapper::toEmployeeSalaryDto)
                .toList();
    }

    public EmployeeSalaryDto getByPeriod(User currentUser, YearMonth period) {
        Salary salary = this.salaryRepository.findBySalaryIdUserIdAndSalaryIdPeriod(currentUser.getUserId(), period)
                .orElseThrow(() -> new InvalidUserException("not found"));
        return this.salaryMapper.toEmployeeSalaryDto(salary);
    }

    public void updateUserLock(UserLockDto userLockDto,
                               HttpServletRequest request,
                               User currentUser
    ) {
        if (isLockOperation(userLockDto.getOperation())) {
            lockUser(userLockDto.getUser().toLowerCase(), request, currentUser);
        } else {
            User user = this.userRepository.findByEmailIgnoreCase(userLockDto.getUser()).orElseThrow(UserNotFoundException::new);
            unlockUser(user, request, currentUser);
        }
    }

    private void unlockUser(User user, HttpServletRequest request, User currentUser) {
        user.setLockedUntil(null);
        this.userRepository.save(user);
        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.UNLOCK_USER,
                request,
                currentUser,
                "Unlock user " + user.getEmail().toLowerCase()
        );

    }


    public void lockUser(final String email, HttpServletRequest request, User currentUser) {
        User user = this.userRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);
        if (user.isAdmin()) {
            if (currentUser == null) {
                return;
            }
            throw new InvalidUserException("Can't lock the ADMINISTRATOR!");
        }

        user.setLockedUntil(LocalDateTime.now().plusDays(3));

        this.userRepository.save(user);

        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.LOCK_USER,
                request,
                currentUser == null ? user : currentUser,
                "Lock user " + email.toLowerCase()
        );
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isLockOperation(UserLockDto.Operation operation) {
        return UserLockDto.Operation.LOCK.equals(operation);
    }

    private boolean isGrantRole(AdminUserRoleUpdateDto.Operation operation) {
        return AdminUserRoleUpdateDto.Operation.GRANT.equals(operation);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername email: {}", email);
        // return new UserAdapter(this.userRepository.findByEmailIgnoreCase(email).orElseThrow(UnAuthorizedUserException::new));
        return new UserAdapter(this.userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found")));
    }

}
