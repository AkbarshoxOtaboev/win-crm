package uz.script.wincrm.security.refreshToken;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uz.script.wincrm.utils.TableName;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.REFRESH_TOKENS)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sessiya avval yaratiladi, token keyinroq (finalizeSession orqali) biriktiriladi — shuning uchun nullable
    @Column(unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;

    private String ipAddress;

    private String userAgent;

    private String deviceName;

    private LocalDateTime lastSeenAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}