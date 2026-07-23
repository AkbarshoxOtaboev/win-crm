package uz.script.wincrm.security.refreshToken;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "Session summary statistics")
public class SessionSummaryResponse {
    private long totalSessions;
    private long onlineSessions;
    private long onlineUsers;
    private long offlineUsers;
}