package uz.script.wincrm.audit;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditResponse {

    private Long id;

    private String username;

    private String entity;

    private String action;

    private String description;

    private LocalDateTime createdAt;

    private String ipAddress;
}
