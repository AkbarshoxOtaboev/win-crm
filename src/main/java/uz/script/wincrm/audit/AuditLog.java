package uz.script.wincrm.audit;
import jakarta.persistence.*;
import lombok.*;
import uz.script.wincrm.utils.TableName;

import java.time.LocalDateTime;

@Entity
@Table(name = TableName.AUDIT_LOGS)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String entity;
    private String action;
    private String httpMethod;
    @Column(length = 500)
    private String requestUrl;
    private String ipAddress;
    @Column(length = 1000)
    private String userAgent;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime createdAt;

}
