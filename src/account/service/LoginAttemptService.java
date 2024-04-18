package account.service;


import account.constants.SecurityEventEnum;
import account.domain.User;
import account.security.service.SecurityEventService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    public static final int MAX_ATTEMPTS = 5;
    private final LoadingCache<String, Integer> attemptsCache;
    private final SecurityEventService securityEventService;
    private final UserService userService;

    public LoginAttemptService(SecurityEventService securityEventService, UserService userService) {
        this.securityEventService = securityEventService;
        this.userService = userService;
        this.attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(
                        new CacheLoader<String, Integer>() {
                            @Override
                            public Integer load(String key) throws Exception {
                                return 0;
                            }
                        }
                );
    }

    public void loginFailed(final String email, final HttpServletRequest request) {
        String key = this.getCacheKey(email, request);
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts < MAX_ATTEMPTS) {
            return;
        }
        User user = new User();
        user.setEmail(email);
        // brute force event
        this.securityEventService.createSecurityEvent(
                SecurityEventEnum.BRUTE_FORCE,
                request,
                user,
                request.getRequestURI()
        );
        // block user
        this.userService.lockUser(email, request, null);
        attemptsCache.put(key, 0);
    }

    public void loginSucceeded(User user, final HttpServletRequest request) {
        String key = this.getCacheKey(user.getEmail().toLowerCase(), request);
        attemptsCache.put(key, 0);
    }

    private String getCacheKey(final String email, HttpServletRequest request) {
        return email + request.getRequestURI();
    }
}
