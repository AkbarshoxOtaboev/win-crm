package uz.script.wincrm.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.ErrorResponse;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.exceptions.UnauthorizedException;
import uz.script.wincrm.exceptions.UserDisabledException;
import uz.script.wincrm.security.blacklist.TokenBlacklistService;
import uz.script.wincrm.security.jwt.JwtService;
import uz.script.wincrm.security.refreshToken.RefreshToken;
import uz.script.wincrm.security.refreshToken.SessionService;
import uz.script.wincrm.users.User;
import uz.script.wincrm.users.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth controller management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    @Auditable(
            action = AuditAction.LOGIN,
            entity = "Authentication"
    )
    @Operation(
            summary = "User login",
            description = "Returns access token, refresh token and session id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {

        User user;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password");
        } catch (DisabledException e) {
            throw new UserDisabledException("User account is disabled");
        }

        // Bitta faol sessiya siyosati: yangi sessiya yaratiladi, eskisi avtomatik o'chiriladi
        RefreshToken session = sessionService.issueSession(
                user.getUsername(),
                resolveIp(httpRequest),
                httpRequest.getHeader("User-Agent")
        );

        String accessToken = jwtService.generateAccessToken(user.getUsername(), session.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), session.getId());

        LocalDateTime refreshExpiresAt = LocalDateTime.ofInstant(
                jwtService.extractExpiration(refreshToken).toInstant(),
                ZoneId.systemDefault()
        );

        sessionService.finalizeSession(session.getId(), refreshToken, refreshExpiresAt);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer")
                        .sessionId(session.getId())
                        .build()
        );
    }


    @PostMapping("/refresh")
    @Auditable(
            action = AuditAction.REFRESH_TOKEN,
            entity = "Authentication"
    )
    @Operation(
            summary = "Refresh access token",
            description = "Generate new access token using refresh token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid refresh token"
            )
    })
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        Long sid = jwtService.extractSid(refreshToken);

        RefreshToken session = sessionService.validateAndTouch(
                sid,
                resolveIp(httpRequest),
                httpRequest.getHeader("User-Agent")
        ).orElseThrow(() -> new UnauthorizedException("Sessiya yopilgan yoki muddati tugagan"));

        String newAccessToken = jwtService.generateAccessToken(session.getUsername(), session.getId());

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer")
                        .sessionId(session.getId())
                        .build()
        );
    }


    @PostMapping("/logout")
    @Auditable(
            action = AuditAction.LOGOUT,
            entity = "Authentication"
    )
    @Operation(
            summary = "User logout",
            description = "Blacklists current access token and revokes the session"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid token"
            )
    })
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("Token not found");
        }

        String token = authHeader.substring(7);

        long remainingTime = jwtService.getRemainingTime(token);

        tokenBlacklistService.blacklist(token, remainingTime);

        Long sid = jwtService.extractSid(token);

        if (sid != null) {
            sessionService.revoke(sid);
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    private String resolveIp(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}