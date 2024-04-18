package account.loader;

import account.constants.AcmeAuthority;
import account.constants.AcmeRole;
import account.domain.Authority;
import account.domain.Role;
import account.repository.AuthorityRepository;
import account.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RoleAuthorityLoader {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public RoleAuthorityLoader(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        createRoleAndAuthorities();
    }

    private void createRoleAndAuthorities() {
        if (authorityRepository.count() != 0) {
            return;
        }

        Authority signupAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.AUTH_SIGNUP));
        Authority changepassAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.AUTH_CHANGEPASS));
        Authority createPaymentAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.PAYMENT_CREATE));
        Authority readPaymentAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.PAYMENT_READ));
        Authority updatePaymentAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.PAYMENT_UPDATE));
        Authority securityAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.SECURITYEVENT_READ));
        Authority adminAuthority = this.authorityRepository.save(new Authority(AcmeAuthority.ADMIN));

        Role userRole = this.roleRepository.save(new Role(AcmeRole.USER, List.of(
                signupAuthority,
                changepassAuthority,
                readPaymentAuthority
        )));
        log.info("ADD USER Role" + userRole.getAuthority());
        Role accountantRole = this.roleRepository.save(new Role(AcmeRole.ACCOUNTANT, List.of(
                signupAuthority,
                changepassAuthority,
                readPaymentAuthority,
                createPaymentAuthority,
                updatePaymentAuthority
        )));
        log.info("ADD ACCOUNTANT Role" + accountantRole.getAuthority());
        Role auditorRole = this.roleRepository.save(new Role(AcmeRole.AUDITOR, List.of(
                signupAuthority,
                securityAuthority
        )));
        log.info("ADD AUDITOR Role" + auditorRole.getAuthority());
        Role administratorRole = this.roleRepository.save(new Role(AcmeRole.ADMINISTRATOR, List.of(
                signupAuthority,
                changepassAuthority,
                adminAuthority
        )));
        log.info("ADD ADMINISTRATOR Role" + administratorRole.getAuthority());
    }
}
