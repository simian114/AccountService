package account.repository;

import account.domain.Salary;
import account.domain.SalaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {

    @Query("SELECT s FROM Salary s JOIN FETCH s.user where s.user.userId = :userId")
    List<Salary> findByUserIdWithUser(@Param("userId") int userId);

    @Query("SELECT s FROM Salary s JOIN FETCH s.user where s.user.userId = :userId order by s.salaryId.period desc")
    List<Salary> findByUserIdWithUserOrderBySalaryIdPeriodDesc(@Param("userId") int userId);


    Optional<Salary> findBySalaryIdUserIdAndSalaryIdPeriod(int userId, YearMonth period);
}
