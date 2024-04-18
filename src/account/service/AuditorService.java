package account.service;

import account.domain.SecurityEvent;
import account.repository.SecurityEventRepository;
import account.web.dto.SecurityEventDto;
import account.web.mapper.SecurityEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditorService {
    private final SecurityEventRepository securityEventRepository;
    private final SecurityEventMapper securityEventMapper;

//    private final

    public Object showAllEvents() {
        return this.securityEventRepository.findAllByOrderByIdAsc().stream()
                .map(securityEventMapper::toDto)
                .collect(Collectors.toList());
    }
}
