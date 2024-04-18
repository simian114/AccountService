package account.web.controller;

import account.domain.User;
import account.service.UserService;
import account.web.dto.UserChangePasswordDto;
import account.web.dto.UserDto;
import account.web.resolver.annotation.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto,
                             HttpServletRequest request,
                             @LoginUser User currentUser
    ) {

        return ResponseEntity.ok().body(this.userService.registerUser(userDto, request, currentUser));
    }

    @PostMapping("/changepass")
    ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordDto userChangePasswordDto,
                                     @LoginUser User currentUser,
                                     HttpServletRequest request
    ) {
        return ResponseEntity.ok().body(this.userService.changePassword(userChangePasswordDto, request, currentUser));
    }
}
