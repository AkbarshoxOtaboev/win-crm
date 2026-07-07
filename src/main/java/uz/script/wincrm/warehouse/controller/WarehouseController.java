package uz.script.wincrm.warehouse.controller;

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
import uz.script.wincrm.warehouse.dto.WarehouseDTO;
import uz.script.wincrm.warehouse.response.WarehouseResponse;
import uz.script.wincrm.warehouse.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse REST API Management Controller")
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('WAREHOUSE_CREATE')")
    @Operation(
            summary = "Create warehouse",
            description = "Only users with WAREHOUSE_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody WarehouseDTO dto) {
        WarehouseResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<WarehouseResponse>builder()
                                .message("Warehouse successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_VIEW')")
    @Operation(
            summary = "Fetch all warehouses",
            description = "Only users with WAREHOUSE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchAllWarehouses() {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseResponse>>builder()
                        .message("All warehouses fetched successfully")
                        .data(service.fetchAllWarehouses())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_VIEW')")
    @Operation(
            summary = "Fetch warehouse by id",
            description = "Only users with WAREHOUSE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<WarehouseResponse>builder()
                        .message("Warehouse found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_EDIT')")
    @Operation(
            summary = "Update warehouse",
            description = "Only users with WAREHOUSE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseDTO dto
    ) {
        WarehouseResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<WarehouseResponse>builder()
                        .message("Warehouse successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_DELETE')")
    @Operation(
            summary = "Delete warehouse",
            description = "Only users with WAREHOUSE_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Warehouse successfully deleted")
                        .build()
        );
    }
}
