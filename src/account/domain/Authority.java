package account.domain;

import account.constants.AcmeAuthority;
import account.repository.AuthorityRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authority_id;

    @Enumerated(EnumType.STRING)
    @Column(unique=true, nullable=false)
    private AcmeAuthority authority;


    public Authority(AcmeAuthority authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.name();
    }
}
