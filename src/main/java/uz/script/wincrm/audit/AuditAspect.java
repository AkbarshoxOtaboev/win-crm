package uz.script.wincrm.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final HttpServletRequest request;

    @AfterReturning("@annotation(auditable)")
    public void audit(JoinPoint joinPoint, Auditable auditable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication != null
                ? authentication.getName()
                : "Anonymous";

        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .entity(auditable.entity())
                .action(auditable.action().name())
                .description(joinPoint.getSignature().getName() + " executed")
                .httpMethod(request.getMethod())
                .requestUrl(request.getRequestURI())
                .ipAddress(getClientIp())
                .userAgent(request.getHeader("User-Agent"))
                .createdAt(LocalDateTime.now())
                .build();

        auditLogService.save(auditLog);
    }
    private String getClientIp() {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        String ip = request.getRemoteAddr();

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }
}