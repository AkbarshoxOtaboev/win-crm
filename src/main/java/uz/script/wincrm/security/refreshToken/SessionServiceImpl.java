package uz.script.wincrm.security.refreshToken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.script.wincrm.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {

    private static final long ONLINE_MINUTES = 5;
    private static final long TOUCH_THROTTLE_SECONDS = 45;

    private final RefreshTokenRepository repository;

    @Override
    public RefreshToken issueSession(String username, String ipAddress, String userAgent) {

        log.info("Issue new session for user {}", username);

        // Bitta user uchun bitta faol sessiya siyosati: yangi login eskisini o'chiradi
        repository.deleteByUsername(username);

        RefreshToken session = RefreshToken.builder()
                .username(username)
                .expiresAt(LocalDateTime.now()) // finalizeSession'da haqiqiy qiymat bilan almashtiriladi
                .revoked(false)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .deviceName(parseDeviceName(userAgent))
                .lastSeenAt(LocalDateTime.now())
                .build();

        RefreshToken saved = repository.save(session);

        log.info("Session {} created for user {}", saved.getId(), username);

        return saved;
    }

    @Override
    public void finalizeSession(Long sessionId, String token, LocalDateTime expiresAt) {

        RefreshToken session = repository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id " + sessionId));

        session.setToken(token);
        session.setExpiresAt(expiresAt);
    }

    @Override
    public List<SessionResponse> listSessions(Long currentSessionId, String usernameFilter, SessionStatusFilter statusFilter) {

        log.info("List sessions, userFilter={}, statusFilter={}", usernameFilter, statusFilter);

        List<RefreshToken> sessions = (usernameFilter == null || usernameFilter.isBlank())
                ? repository.findAllByOrderByCreatedAtDesc()
                : repository.findAllByUsernameOrderByCreatedAtDesc(usernameFilter);

        LocalDateTime now = LocalDateTime.now();

        return sessions.stream()
                .map(s -> mapToResponse(s, currentSessionId, now))
                .filter(r -> matchesStatus(r, statusFilter))
                .toList();
    }

    @Override
    public SessionSummaryResponse summary() {

        log.info("Build session summary");

        List<RefreshToken> sessions = repository.findAll();
        LocalDateTime now = LocalDateTime.now();

        Set<String> onlineUsernames = sessions.stream()
                .filter(s -> isOnline(s, now))
                .map(RefreshToken::getUsername)
                .collect(Collectors.toSet());

        Set<String> allUsernames = sessions.stream()
                .map(RefreshToken::getUsername)
                .collect(Collectors.toSet());

        long onlineSessions = sessions.stream()
                .filter(s -> isOnline(s, now))
                .count();

        return SessionSummaryResponse.builder()
                .totalSessions(sessions.size())
                .onlineSessions(onlineSessions)
                .onlineUsers(onlineUsernames.size())
                .offlineUsers(allUsernames.size() - onlineUsernames.size())
                .build();
    }

    @Override
    public void revoke(Long id) {

        log.info("Revoke session {}", id);

        RefreshToken session = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id " + id));

        session.setRevoked(true);
    }

    @Override
    public void revokeAllExcept(Long currentSessionId) {

        log.info("Revoke all sessions except {}", currentSessionId);

        repository.findAll().forEach(session -> {
            if (currentSessionId == null || !session.getId().equals(currentSessionId)) {
                session.setRevoked(true);
            }
        });
    }

    @Override
    public void revokeAllByUsername(String username) {

        log.info("Revoke all sessions for user {}", username);

        repository.findAllByUsernameOrderByCreatedAtDesc(username)
                .forEach(session -> session.setRevoked(true));
    }

    @Override
    public Optional<RefreshToken> validateAndTouch(Long sessionId, String ipAddress, String userAgent) {

        if (sessionId == null) {
            return Optional.empty();
        }

        Optional<RefreshToken> found = repository.findById(sessionId);

        if (found.isEmpty()) {
            return Optional.empty();
        }

        RefreshToken session = found.get();
        LocalDateTime now = LocalDateTime.now();

        if (session.isRevoked() || session.getExpiresAt().isBefore(now)) {
            return Optional.empty();
        }

        if (session.getLastSeenAt() == null ||
                ChronoUnit.SECONDS.between(session.getLastSeenAt(), now) >= TOUCH_THROTTLE_SECONDS) {

            session.setLastSeenAt(now);

            if (ipAddress != null && !ipAddress.isBlank()) {
                session.setIpAddress(ipAddress);
            }
        }

        return Optional.of(session);
    }

    private SessionResponse mapToResponse(RefreshToken session, Long currentSessionId, LocalDateTime now) {

        boolean expired = session.getExpiresAt().isBefore(now);
        boolean online = isOnline(session, now);

        return SessionResponse.builder()
                .id(session.getId())
                .username(session.getUsername())
                .ipAddress(session.getIpAddress())
                .userAgent(session.getUserAgent())
                .deviceName(session.getDeviceName())
                .createdAt(session.getCreatedAt())
                .lastSeenAt(session.getLastSeenAt())
                .expiresAt(session.getExpiresAt())
                .online(online)
                .current(currentSessionId != null && session.getId().equals(currentSessionId))
                .revoked(session.isRevoked())
                .expired(expired)
                .build();
    }

    private boolean isOnline(RefreshToken session, LocalDateTime now) {
        return !session.isRevoked()
                && session.getExpiresAt().isAfter(now)
                && session.getLastSeenAt() != null
                && session.getLastSeenAt().isAfter(now.minus(ONLINE_MINUTES, ChronoUnit.MINUTES));
    }

    private boolean matchesStatus(SessionResponse response, SessionStatusFilter filter) {

        if (filter == null || filter == SessionStatusFilter.ALL) {
            return true;
        }

        return switch (filter) {
            case ONLINE -> response.isOnline();
            case OFFLINE -> !response.isOnline();
            default -> true;
        };
    }

    private String parseDeviceName(String userAgent) {

        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown device";
        }

        String ua = userAgent.toLowerCase();

        String os;
        if (ua.contains("windows")) {
            os = "Windows";
        } else if (ua.contains("mac os") || ua.contains("macintosh")) {
            os = "macOS";
        } else if (ua.contains("android")) {
            os = "Android";
        } else if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) {
            os = "iOS";
        } else if (ua.contains("linux")) {
            os = "Linux";
        } else {
            os = "Unknown OS";
        }

        String browser;
        if (ua.contains("edg/")) {
            browser = "Edge";
        } else if (ua.contains("opr/") || ua.contains("opera")) {
            browser = "Opera";
        } else if (ua.contains("chrome/")) {
            browser = "Chrome";
        } else if (ua.contains("firefox/")) {
            browser = "Firefox";
        } else if (ua.contains("safari/")) {
            browser = "Safari";
        } else {
            browser = "Unknown browser";
        }

        boolean mobile = ua.contains("mobile");

        return mobile ? ("Mobile " + browser + " / " + os) : (browser + " / " + os);
    }
}