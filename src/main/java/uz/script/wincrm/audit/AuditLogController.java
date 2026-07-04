package uz.script.wincrm.audit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit log management rest api")
public class AuditLogController {
    private final AuditLogService service;

    @GetMapping("/logs")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "All logs")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = AuditResponse.class))
    ))
    public ResponseEntity<?> allLogs(){
        return ResponseEntity.ok().body(
                RestApiResponse.<List<AuditResponse>>builder()
                        .message("All logs")
                        .data(service.getAll())
                        .build()
        );
    }
}
