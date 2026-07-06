package uz.script.wincrm.suppliers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import uz.script.wincrm.suppliers.SupplierService;
import uz.script.wincrm.suppliers.dto.SupplierDTO;
import uz.script.wincrm.suppliers.dto.SupplierFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierResponse;
import uz.script.wincrm.utils.RestApiResponse;
import uz.script.wincrm.utils.Status;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier rest api management controller")
public class SupplierController {

    private final SupplierService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SUPPLIER_CREATE')")
    @Operation(
            summary = "Create supplier",
            description = "Only users with SUPPLIER_CREATE permission can use it."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody SupplierDTO dto) {

        SupplierResponse response = service.create(dto);

        return ResponseEntity.status(201).body(
                RestApiResponse.<SupplierResponse>builder()
                        .message("Supplier successfully created")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW')")
    @Operation(
            summary = "Fetch all suppliers",
            description = "Only users with SUPPLIER_VIEW permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchAll(Pageable pageable) {

        return ResponseEntity.ok(
                RestApiResponse.<Page<SupplierResponse>>builder()
                        .message("All suppliers fetched successfully")
                        .data(service.findAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW')")
    @Operation(
            summary = "Fetch supplier by id",
            description = "Only users with SUPPLIER_VIEW permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return ResponseEntity.ok(
                RestApiResponse.<SupplierResponse>builder()
                        .message("Supplier found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_EDIT')")
    @Operation(
            summary = "Update supplier",
            description = "Only users with SUPPLIER_EDIT permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDTO dto
    ) {

        SupplierResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<SupplierResponse>builder()
                        .message("Supplier successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_DELETE')")
    @Operation(
            summary = "Delete supplier",
            description = "Only users with SUPPLIER_DELETE permission can use it."
    )
    public ResponseEntity<?> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Supplier successfully deleted")
                        .build()
        );
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW')")
    @Operation(
            summary = "Filter suppliers",
            description = "Filter suppliers by different parameters."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> filter(
            @RequestBody SupplierFilterDTO filter,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                RestApiResponse.<Page<SupplierResponse>>builder()
                        .message("Suppliers filtered successfully")
                        .data(service.filter(filter, pageable))
                        .build()
        );
    }

    @PutMapping("/change/status/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_EDIT')")
    @Operation(
            summary = "Change supplier status",
            description = "Only users with SUPPLIER_EDIT permission can use it."
    )
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestParam Status status
    ) {

        SupplierResponse response = service.changeStatus(id, status);

        return ResponseEntity.ok(
                RestApiResponse.<SupplierResponse>builder()
                        .message("Supplier status successfully changed")
                        .data(response)
                        .build()
        );
    }

}