package account.repository;

import account.domain.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Integer> {
    List<SecurityEvent> findAllByOrderByIdAsc();
}
