package account.web.mapper;

import account.domain.Salary;
import account.domain.SalaryId;
import account.domain.User;
import account.web.dto.EmployeeSalaryDto;
import org.springframework.stereotype.Component;

@Component
public class SalaryMapper {
    public Salary toEntity(EmployeeSalaryDto employeeSalaryDto, User user) {
        Salary salary = new Salary();
        if (user != null) {
            salary.setUser(user);
            SalaryId salaryId = new SalaryId(user.getUserId(), employeeSalaryDto.getPeriod());
            salary.setSalaryId(salaryId);
            salary.setEmployee(user.getEmail());
        }
        salary.setSalary(employeeSalaryDto.getSalary());
        return salary;
    }

    public EmployeeSalaryDto toEmployeeSalaryDto(Salary salary) {
        EmployeeSalaryDto employeeSalaryDto = new EmployeeSalaryDto();
        employeeSalaryDto.setPeriod(salary.getSalaryId().getPeriod());
        employeeSalaryDto.setSalary(salary.getSalary());
        User user = salary.getUser();
        if (user != null) {
            employeeSalaryDto.setName(user.getName());
            employeeSalaryDto.setLastname(user.getLastname());
        }
        return employeeSalaryDto;
    }
}
