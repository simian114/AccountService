package account.web.controller;

import account.constants.AcmeRole;
import account.domain.User;
import account.service.UserService;
import account.web.dto.AdminUserRoleUpdateDto;
import account.web.dto.StatusDto;
import account.web.dto.UserLockDto;
import account.web.dto.UserWithStatusDto;
import account.web.mapper.UserMapper;
import account.web.resolver.annotation.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final UserMapper userMapper;

    @DeleteMapping({"/user", "/user/", "/user/{email}"})
    ResponseEntity<?> deleteUser(@PathVariable String email,
                                 HttpServletRequest request,
                                 @LoginUser User currentUser
    ) {
        AcmeRole.ADMINISTRATOR.getWithPrefix();
        log.info("Delete user: {}", email);
        this.userService.deleteUser(email, request, currentUser);
        return ResponseEntity.ok().body(new UserWithStatusDto(email, "Deleted successfully!"));
    }

    @PutMapping({"/user/role", "/user/role/"})
    ResponseEntity<?> updateUserRole(@Valid @RequestBody AdminUserRoleUpdateDto adminUserRoleUpdateDto,
                                     HttpServletRequest request,
                                     @LoginUser User currentUser
    ) {
        log.info("Update user role: {}", adminUserRoleUpdateDto);
        return ResponseEntity.ok(this.userService.updateUserRole(adminUserRoleUpdateDto, request, currentUser));
    }

    @GetMapping({"/user", "/user/"})
    ResponseEntity<?> getAllUser() {
        log.info("get all user");
        return ResponseEntity.ok(userService
                .getAllUsers()
                .stream()
                .map(this.userMapper::toDto)
        );
    }

    @PutMapping({"/user/access", "/user/access/"})
    ResponseEntity<?> updateUserLock(@Valid @RequestBody UserLockDto userLockDto,
                                     HttpServletRequest request,
                                     @LoginUser User currentUser
                                     ) {
        this.userService.updateUserLock(userLockDto, request, currentUser);
        return ResponseEntity.ok(new StatusDto("User " + userLockDto.getUser().toLowerCase() + (userLockDto.getOperation().equals(UserLockDto.Operation.LOCK) ? " locked!" : " unlocked!")));
    }
}
