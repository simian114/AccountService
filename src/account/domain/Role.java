package account.domain;

import account.constants.AcmeRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Role implements GrantedAuthority {
    private static final Logger log = LoggerFactory.getLogger(Role.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private AcmeRole name;

    public Role(AcmeRole acmeRole) {
        this.name = acmeRole;
    }

    public Role(AcmeRole acmeRole, List<Authority> authorities) {
        this.name = acmeRole;
        this.authorities = authorities;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return "ROLE_" + this.name.name();
    }

    // domain logic
    @JsonIgnore
    public boolean isAdminRoleGroup() {
        return this.name.equals(AcmeRole.ADMINISTRATOR);
    }
}
