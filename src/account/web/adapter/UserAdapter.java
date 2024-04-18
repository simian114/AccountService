package account.web.adapter;

import account.domain.Authority;
import account.domain.Role;
import account.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Slf4j
public class UserAdapter implements UserDetails {
    private final User user;

    public UserAdapter(User user) {
        log.info("user adapter user: {}", user);
        this.user = user;
    }

    /**
     * User 도메인 객체는 이 메서드의 존재 자체를 알 필요가 없다.
     * 왜냐면 요청이 들어왔을 때 Security가 보안 처리를 위해 이 메서드를 사용하는거기 때문임.
     * 만약 우리 코드에서 Role 또는 Authority가 필요하다면 그냥 user.role / user.role.authority를 사용.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = this.user.getRoles().stream()
                .map(Role::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        for (Role role : this.user.getRoles()) {
            authorities.addAll(role.getAuthorities().stream()
                    .map(Authority::getAuthority)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet())
            );
        }
        log.info("authorities: {}", authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getLockedUntil() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 이런 로직은 나중에 세션이 없어졌는지 확인해서 false / true 결정하게 만들어야하는듯?
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
