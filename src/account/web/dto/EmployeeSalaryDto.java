package account.web.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.YearMonth;

/*
    요청을 받을 때는 deserialized로 string을 객체로 변환 -> getter 필요
    응답을 줄 때는 객체를 serialized로 string로 변환 -> setter 필요
    @Setter(AccessLevel.NONE) -> setter를 없앰
    @Getter(AccessLevel.NONE) -> getter를 없앰
 */
@Getter
@Setter
@ToString
public class EmployeeSalaryDto {
    @NotBlank
    @Setter(AccessLevel.NONE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String employee;

    /*
    when serialized, then MM-yyyy
    when deserialized, then MMMM-yyyy
     */
    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth period;

    @Min(value = 0, message = "Salary must be non negative!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long salary;

    @Getter(AccessLevel.NONE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    @Getter(AccessLevel.NONE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastname;

    @JsonFormat(pattern = "MMMM-yyyy")
    public YearMonth getPeriod() {
        return this.period;
    }

    @JsonProperty("salary")
    public String getFormattedSalary() {
        long dollar = this.salary / 100;
        long cent = this.salary % 100;
        return String.format("%d dollar(s) %d cent(s)", dollar, cent);
    }
}
