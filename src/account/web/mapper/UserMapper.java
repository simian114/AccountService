package account.web.mapper;

import account.domain.Role;
import account.domain.User;
import account.web.dto.UserChangePasswordDto;
import account.web.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setEmail(user.getEmail().toLowerCase());
        userDto.setLastname(user.getLastname());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());

        if (!user.getRoles().isEmpty()) {
            userDto.setRoles(user.getRoles()
                    .stream()
                    .map(Role::getAuthority)
                    .toList()
            );
        }

        return userDto;
    }

    public User toEntityFromUserSignupDto(UserDto userDto) {
        User user = new User();
        if (userDto.getId() > 0) {
            user.setUserId(user.getUserId());
        }
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setLastname(userDto.getLastname());
        return user;
    }

    public UserChangePasswordDto toChangePasswordDto(User user) {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto();
        userChangePasswordDto.setStatus("The password has been updated successfully");
        userChangePasswordDto.setEmail(user.getEmail());
        return userChangePasswordDto;
    }
}

