package account.web.controller;

import account.repository.SecurityEventRepository;
import account.service.AuditorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {
    private final AuditorService auditorService;

    @GetMapping({ "/events", "/events/" })
    ResponseEntity<?> showAllSecurityEvents() {
        log.info("SHOW ALL security events handler");
        return ResponseEntity.ok((auditorService.showAllEvents()));
    }
}
