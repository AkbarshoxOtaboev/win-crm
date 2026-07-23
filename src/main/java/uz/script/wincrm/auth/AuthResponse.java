package uz.script.wincrm.auth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Access token and refresh token")
public class AuthResponse {
    @Schema(description = "Access token", example = "exdpmfsdh34ghfsdh....")
    private String accessToken;
    @Schema(description = "Refresh token", example = "exdpmfgrtsf...")
    private String refreshToken;
    @Schema(description = "Token type", example = "Bearer")
    private String tokenType;
    @Schema(description = "Session id", example = "42")
    private Long sessionId;
}