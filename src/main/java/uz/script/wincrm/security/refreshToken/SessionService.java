package uz.script.wincrm.security.refreshToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionService {

    RefreshToken issueSession(String username, String ipAddress, String userAgent);

    void finalizeSession(Long sessionId, String token, LocalDateTime expiresAt);

    List<SessionResponse> listSessions(Long currentSessionId, String usernameFilter, SessionStatusFilter statusFilter);

    SessionSummaryResponse summary();

    void revoke(Long id);

    void revokeAllExcept(Long currentSessionId);

    void revokeAllByUsername(String username);

    /**
     * Sessiya faolligini tekshiradi va agar valid bo'lsa lastSeenAt/ipAddress'ni yangilaydi
     * (45 soniyalik throttle bilan — har so'rovda DB'ni spam qilmaydi).
     */
    Optional<RefreshToken> validateAndTouch(Long sessionId, String ipAddress, String userAgent);
}