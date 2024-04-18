package account.web.controller;

import account.domain.User;
import account.service.PaymentService;
import account.service.UserService;
import account.web.dto.EmployeeSalaryDto;
import account.web.dto.StatusDto;
import account.web.mapper.UserMapper;
import account.web.resolver.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController()
@Validated
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/api/empl/payment")
    ResponseEntity<?> showMyPayments(
            @RequestParam(name = "period", required = false) @DateTimeFormat(pattern = "MM-yyyy") YearMonth period,
            @LoginUser User currentUser) {
        System.out.println(period);
        return ResponseEntity.ok().body(
                period == null
                        ? this.userService.getAllPayments(currentUser)
                        : this.userService.getByPeriod(currentUser, period)
        );
    }

    @PostMapping("/api/acct/payments")
    ResponseEntity<?> storePayments(@RequestBody List<@Valid EmployeeSalaryDto> payments) {
        this.paymentService.storePayments(payments);
        return ResponseEntity.ok().body(new StatusDto("Added successfully!"));
    }

    @PutMapping("/api/acct/payments")
    ResponseEntity<?> updatePayments(@Valid @RequestBody EmployeeSalaryDto payment) {
        this.paymentService.updatePayment(payment);
        return ResponseEntity.ok().body(new StatusDto("Updated successfully!"));
    }

}
