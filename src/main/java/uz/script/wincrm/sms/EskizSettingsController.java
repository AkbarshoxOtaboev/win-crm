package uz.script.wincrm.sms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/eskiz-settings")
@RequiredArgsConstructor
@Tag(name = "Eskiz Settings REST API", description = "Eskiz.uz SMS login credentials management")
public class EskizSettingsController {

    private final EskizSettingsService settingsService;
    private final EskizTokenService tokenService;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Fetch Eskiz.uz settings status",
            description = "Only users with SUPER_ADMIN permission can use this endpoint. " +
                    "Password is never returned; email is masked."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EskizSettingsResponse.class)
            )
    )
    public ResponseEntity<?> getSettings() {
        EskizSettingsResponse response = settingsService.getSettings();

        return ResponseEntity.ok(
                RestApiResponse.<EskizSettingsResponse>builder()
                        .message("Eskiz settings fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Save Eskiz.uz login credentials",
            description = "Only users with ESKIZ_SETTINGS_MANAGE permission can use this endpoint. " +
                    "Password is encrypted before being persisted; any cached token is invalidated."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EskizSettingsResponse.class)
            )
    )
    public ResponseEntity<?> saveCredentials(@Valid @RequestBody EskizCredentialsDto dto) {
        EskizSettingsResponse response = settingsService.saveCredentials(dto);

        return ResponseEntity.ok(
                RestApiResponse.<EskizSettingsResponse>builder()
                        .message("Eskiz credentials saved successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/token")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Save Eskiz.uz login credentials",
            description = "Only users with ESKIZ_SETTINGS_MANAGE permission can use this endpoint. " +
                    "Password is encrypted before being persisted; any cached token is invalidated."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json")
    )
    public ResponseEntity<?> getToken() {

        return ResponseEntity.ok(tokenService.getToken());
    }
}