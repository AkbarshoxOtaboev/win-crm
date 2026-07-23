package uz.script.wincrm.security.refreshToken;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.security.jwt.JwtService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "Active sessions management controller")
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SESSION_READ')")
    @Operation(summary = "List sessions", description = "Only users with SESSION_READ permission can use it.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SessionResponse.class))
            ))
    public ResponseEntity<?> listSessions(
            HttpServletRequest request,
            @RequestParam(required = false) String username,
            @RequestParam(required = false, defaultValue = "ALL") SessionStatusFilter status
    ) {
        Long currentSid = extractCurrentSid(request);

        return ResponseEntity.ok().body(
                RestApiResponse.<List<SessionResponse>>builder()
                        .message("Sessions fetched successfully")
                        .data(sessionService.listSessions(currentSid, username, status))
                        .build()
        );
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('SESSION_READ')")
    @Operation(summary = "Session summary", description = "Only users with SESSION_READ permission can use it.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SessionSummaryResponse.class)
            ))
    public ResponseEntity<?> summary() {
        return ResponseEntity.ok().body(
                RestApiResponse.<SessionSummaryResponse>builder()
                        .message("Session summary fetched successfully")
                        .data(sessionService.summary())
                        .build()
        );
    }

    @PostMapping("/heartbeat")
    @Operation(
            summary = "Session heartbeat",
            description = "Joriy sessiyani onlayn deb belgilaydi. Diqqat: JwtFilter har qanday " +
                    "autentifikatsiyalangan so'rovda ham sessiyani avtomatik yangilaydi (45s throttle bilan); " +
                    "bu endpoint frontend sahifa faol lekin boshqa API chaqiruvi bo'lmagan holatlar uchun."
    )
    public ResponseEntity<?> heartbeat() {
        return ResponseEntity.ok().body(
                RestApiResponse.<Void>builder().message("Heartbeat received").build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SESSION_CREATE')")
    @Auditable(action = AuditAction.UPDATE, entity = "Session")
    @Operation(summary = "Revoke session", description = "Only users with SESSION_WRITE permission can use it.")
    public ResponseEntity<?> revoke(@PathVariable Long id) {

        sessionService.revoke(id);

        return ResponseEntity.ok().body(
                RestApiResponse.<Void>builder().message("Session revoked successfully").build()
        );
    }

    @DeleteMapping("")
    @PreAuthorize("hasAuthority('SESSION_CREATE')")
    @Auditable(action = AuditAction.UPDATE, entity = "Session")
    @Operation(summary = "Revoke all other sessions", description = "Only users with SESSION_WRITE permission can use it.")
    public ResponseEntity<?> revokeAllExceptCurrent(HttpServletRequest request) {

        Long currentSid = extractCurrentSid(request);

        sessionService.revokeAllExcept(currentSid);

        return ResponseEntity.ok().body(
                RestApiResponse.<Void>builder().message("All other sessions revoked successfully").build()
        );
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasAuthority('SESSION_CREATE')")
    @Auditable(action = AuditAction.UPDATE, entity = "Session")
    @Operation(summary = "Revoke all sessions for a user", description = "Only users with SESSION_WRITE permission can use it.")
    public ResponseEntity<?> revokeAllByUsername(@PathVariable String username) {

        sessionService.revokeAllByUsername(username);

        return ResponseEntity.ok().body(
                RestApiResponse.<Void>builder().message("All sessions for user revoked successfully").build()
        );
    }

    private Long extractCurrentSid(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return jwtService.extractSid(header.substring(7));
    }
}