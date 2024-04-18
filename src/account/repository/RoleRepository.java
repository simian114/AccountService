package account.repository;

import account.constants.AcmeRole;
import account.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(AcmeRole acmeRole);
}
