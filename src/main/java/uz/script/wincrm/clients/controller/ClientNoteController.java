package uz.script.wincrm.clients.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.clients.enums.ReminderStatus;
import uz.script.wincrm.clients.dto.ClientNoteDTO;
import uz.script.wincrm.clients.response.ClientNoteResponse;
import uz.script.wincrm.clients.service.ClientNoteService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/client-notes")
@RequiredArgsConstructor
@Tag(name = "Client Note REST API Management Controller")
public class ClientNoteController {

    private final ClientNoteService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_CREATE')")
    @Operation(
            summary = "Create client note",
            description = "Only users with CLIENT_NOTE_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientNoteResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody ClientNoteDTO dto) {
        ClientNoteResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<ClientNoteResponse>builder()
                                .message("Client note successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_VIEW')")
    @Operation(
            summary = "Fetch client note by id",
            description = "Only users with CLIENT_NOTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientNoteResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<ClientNoteResponse>builder()
                        .message("Client note found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_VIEW')")
    @Operation(
            summary = "Fetch notes by client",
            description = "Only users with CLIENT_NOTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = ClientNoteResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<ClientNoteResponse>>builder()
                        .message("Client notes fetched successfully")
                        .data(service.fetchByClient(clientId))
                        .build()
        );
    }

    @GetMapping("/reminders/due")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_VIEW')")
    @Operation(
            summary = "Fetch due reminders",
            description = "Returns notes whose reminder date has arrived and status is still PENDING. " +
                    "Only users with CLIENT_NOTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = ClientNoteResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchDueReminders() {
        return ResponseEntity.ok(
                RestApiResponse.<List<ClientNoteResponse>>builder()
                        .message("Due reminders fetched successfully")
                        .data(service.fetchDueReminders())
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_EDIT')")
    @Operation(
            summary = "Update client note",
            description = "Only users with CLIENT_NOTE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientNoteResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientNoteDTO dto
    ) {
        ClientNoteResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<ClientNoteResponse>builder()
                        .message("Client note successfully updated")
                        .data(response)
                        .build()
        );
    }

    @PatchMapping("/{id}/reminder-status")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_EDIT')")
    @Operation(
            summary = "Update reminder status",
            description = "Only users with CLIENT_NOTE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientNoteResponse.class)
            )
    )
    public ResponseEntity<?> updateReminderStatus(
            @PathVariable Long id,
            @RequestParam ReminderStatus status
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<ClientNoteResponse>builder()
                        .message("Reminder status successfully updated")
                        .data(service.updateReminderStatus(id, status))
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CLIENT_NOTE_DELETE')")
    @Operation(
            summary = "Delete client note",
            description = "Only users with CLIENT_NOTE_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Client note successfully deleted")
                        .build()
        );
    }
}