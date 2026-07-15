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
import uz.script.wincrm.clients.dto.ClientGroupDTO;
import uz.script.wincrm.clients.response.ClientGroupResponse;
import uz.script.wincrm.clients.service.ClientGroupService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/client-groups")
@RequiredArgsConstructor
@Tag(name = "Client Group REST API Management Controller")
public class ClientGroupController {

    private final ClientGroupService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT_GROUP_CREATE')")
    @Operation(
            summary = "Create client group",
            description = "Only users with CLIENT_GROUP_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientGroupResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody ClientGroupDTO dto) {

        ClientGroupResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<ClientGroupResponse>builder()
                                .message("Client group successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENT_GROUP_VIEW')")
    @Operation(
            summary = "Fetch all client groups",
            description = "Only users with CLIENT_GROUP_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = ClientGroupResponse.class)
                    )
            )
    )
    public ResponseEntity<?> findAll() {

        return ResponseEntity.ok(
                RestApiResponse.<List<ClientGroupResponse>>builder()
                        .message("All client groups fetched successfully")
                        .data(service.findAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_GROUP_VIEW')")
    @Operation(
            summary = "Fetch client group by id",
            description = "Only users with CLIENT_GROUP_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientGroupResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return ResponseEntity.ok(
                RestApiResponse.<ClientGroupResponse>builder()
                        .message("Client group found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('CLIENT_GROUP_EDIT')")
    @Operation(
            summary = "Update client group",
            description = "Only users with CLIENT_GROUP_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientGroupResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientGroupDTO dto
    ) {

        ClientGroupResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<ClientGroupResponse>builder()
                        .message("Client group successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CLIENT_GROUP_DELETE')")
    @Operation(
            summary = "Delete client group",
            description = "Only users with CLIENT_GROUP_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Client group successfully deleted")
                        .build()
        );
    }
}