package account.service;

import account.domain.Salary;
import account.domain.SalaryId;
import account.domain.User;
import account.repository.SalaryRepository;
import account.repository.UserRepository;
import account.web.dto.EmployeeSalaryDto;
import account.web.exception.exceptions.InvalidUserException;
import account.web.mapper.SalaryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final SalaryMapper salaryMapper;


    public void storePayments(List<EmployeeSalaryDto> payments) {
        // NOTE:
        // if it already exist then throw error
        List<Salary> list = payments
                .stream()
                .map(payment -> {
                    // find by payment
                    User user = this.userRepository.findByEmailIgnoreCase(payment.getEmployee()).orElseThrow(() -> new InvalidUserException("not found"));
                    if (this.salaryRepository.existsById(new SalaryId(user.getUserId(), payment.getPeriod()))) {
                        throw new InvalidUserException("payment already exists");
                    }
                    return this.salaryMapper.toEntity(payment, user);
                })
                .toList();
        this.salaryRepository.saveAll(list);
    }

    public void updatePayment(EmployeeSalaryDto payment) {
        User user = this.userRepository.findByEmailIgnoreCase(payment.getEmployee()).orElseThrow(() -> new InvalidUserException("not found"));
        Salary salary = this.salaryRepository.findBySalaryIdUserIdAndSalaryIdPeriod(user.getUserId(), payment.getPeriod()).orElseThrow(() -> new InvalidUserException("not found"));
        salary.setSalary(payment.getSalary());
        this.salaryRepository.save(salary);
    }
}
