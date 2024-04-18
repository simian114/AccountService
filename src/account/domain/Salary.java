package account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Salary {
    @EmbeddedId
    private SalaryId salaryId;

    @Column(nullable = false)
    @Min(0)
    private long salary;

    @Column(nullable = false)
    private String employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // SalaryId 클래스의 userId 필드와 매핑
    @JoinColumn(name = "user_id")
    private User user;
}