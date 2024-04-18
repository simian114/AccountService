package account.security.config;

import account.security.exception.handler.RestAuthenticationEntryPoint;
import account.security.exception.handler.CustomAccessDeniedHandler;
import account.constants.AcmeRole;
import account.security.service.SecurityEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    static private final int MIN_PASSWORD_LENGTH = 13;
    private final SecurityEventService securityEventService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(MIN_PASSWORD_LENGTH);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(securityEventService);
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher ) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        // all
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        // user, accountant, admin
                        .requestMatchers(HttpMethod.POST, "/api/auth/changepass", "/api/auth/changepass/").hasAnyRole(
                                AcmeRole.USER.name(),
                                AcmeRole.ACCOUNTANT.name(),
                                AcmeRole.ADMINISTRATOR.name()
                        )
                        // user, accountant
                        .requestMatchers(HttpMethod.GET, "/api/empl/payment", "/api/empl/payment/").hasAnyRole(
                                AcmeRole.USER.name(),
                                AcmeRole.ACCOUNTANT.name()
                        )
                        // accountant
                        .requestMatchers(HttpMethod.POST, "/api/acct/payments", "/api/acct/payments/").hasAnyRole(
                                AcmeRole.ACCOUNTANT.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/api/acct/payments", "/api/acct/payments/").hasAnyRole(
                                AcmeRole.ACCOUNTANT.name()
                        )
                        // auditor
                        .requestMatchers(HttpMethod.GET, "/api/security/events", "/api/security/events/").hasAnyRole(
                                AcmeRole.AUDITOR.name()
                        )
                        // admin
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/user", "/api/admin/user/{email}", "/api/admin/user/").hasAnyRole(
                                AcmeRole.ADMINISTRATOR.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/role", "/api/admin/user/role/").hasAnyRole(
                                AcmeRole.ADMINISTRATOR.name()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/admin/user", "/api/admin/user/").hasAnyRole(
                                AcmeRole.ADMINISTRATOR.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/access", "/api/admin/user/access/").hasRole(
                                AcmeRole.ADMINISTRATOR.name()
                        )
                        // h2 db
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        // rest
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
