package account.security.events;

import account.constants.SecurityEventEnum;
import account.domain.User;
import account.service.LoginAttemptService;
import account.security.service.SecurityEventService;
import account.web.adapter.UserAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEventsListener {
    private final SecurityEventService securityEventService;
    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user = ((UserAdapter) success.getAuthentication().getPrincipal()).getUser();
        loginAttemptService.loginSucceeded(user, request);
        log.info("Authentication success");
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent failureEvent) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        User user = new User();
        user.setEmail((String) failureEvent.getAuthentication().getPrincipal());
        securityEventService.createSecurityEvent(
                SecurityEventEnum.LOGIN_FAILED,
                request,
                user,
                request.getRequestURI()
        );
        loginAttemptService.loginFailed(user.getEmail().toLowerCase(), request);
    }
}
