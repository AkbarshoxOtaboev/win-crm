package uz.script.wincrm.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    @Override
    public void save(AuditLog auditLog) {
        auditLog.setCreatedAt(LocalDateTime.now());
        repository.save(auditLog);
    }

    @Override
    public List<AuditResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(log -> AuditResponse.builder()
                        .id(log.getId())
                        .username(log.getUsername())
                        .entity(log.getEntity())
                        .action(log.getAction())
                        .description(log.getDescription())
                        .createdAt(log.getCreatedAt())
                        .ipAddress(log.getIpAddress())
                        .build())
                .toList();
    }
}