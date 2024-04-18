package account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class SalaryId implements Serializable {
    @Column(name = "user_id")
    private int userId;
    private YearMonth period;
}
