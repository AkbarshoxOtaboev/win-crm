package uz.script.wincrm.clients;

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
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client REST API Management Controller")
public class ClientController {

    private final ClientService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT_CREATE')")
    @Operation(
            summary = "Create client",
            description = "Only users with CLIENT_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody ClientDTO dto) {
        ClientResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<ClientResponse>builder()
                                .message("Client successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENT_VIEW')")
    @Operation(
            summary = "Fetch all clients",
            description = "Only users with CLIENT_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = ClientResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchAllClients() {
        return ResponseEntity.ok(
                RestApiResponse.<List<ClientResponse>>builder()
                        .message("All clients fetched successfully")
                        .data(service.fetchAllClients())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_VIEW')")
    @Operation(
            summary = "Fetch client by id",
            description = "Only users with CLIENT_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<ClientResponse>builder()
                        .message("Client found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('CLIENT_EDIT')")
    @Operation(
            summary = "Update client",
            description = "Only users with CLIENT_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientDTO dto
    ) {
        ClientResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<ClientResponse>builder()
                        .message("Client successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    @Operation(
            summary = "Delete client",
            description = "Only users with CLIENT_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Client successfully deleted")
                        .build()
        );
    }
}