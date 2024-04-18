package account.security.exception.handler;

import account.constants.SecurityEventEnum;
import account.domain.User;
import account.security.service.SecurityEventService;
import account.web.adapter.UserAdapter;
import account.web.exception.ErrorResponse.CustomErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 403 error handler
 */
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityEventService securityEventService;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((UserAdapter) authentication.getPrincipal()).getUser();
        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.ACCESS_DENIED,
                request,
                currentUser,
                request.getRequestURI()
        );
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
                new CustomErrorResponse(LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        "Access Denied!",
                        request.getRequestURI()
                ).toString()
        );
    }
}
