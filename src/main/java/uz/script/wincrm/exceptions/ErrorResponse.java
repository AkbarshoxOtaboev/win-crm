package uz.script.wincrm.exceptions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {
    @Schema(description = "Http status code", example = "404")
    private int status;
    @Schema(description = "Error phrase", example = "NOT_FOUND")
    private String error;
    @Schema(description = "Error message", example = "Resource not found")
    private String message;
    @Schema(description = "Path", example = "/api/users")
    private String path;
    @Schema(description = "Error time", example = "2026-02-23T11:07:57.498671342")
    private String timestamp;
}