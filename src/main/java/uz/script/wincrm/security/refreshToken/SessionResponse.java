package uz.script.wincrm.security.refreshToken;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "Active session response")
public class SessionResponse {
    private Long id;
    private String username;
    private String ipAddress;
    private String userAgent;
    private String deviceName;
    private LocalDateTime createdAt;
    private LocalDateTime lastSeenAt;
    private LocalDateTime expiresAt;
    private boolean online;
    private boolean current;
    private boolean revoked;
    private boolean expired;
}