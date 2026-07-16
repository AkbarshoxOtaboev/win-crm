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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.suppliers.dto.SupplierPaymentDTO;
import uz.script.wincrm.suppliers.dto.SupplierPaymentFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierPaymentResponse;
import uz.script.wincrm.suppliers.service.SupplierPaymentService;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/supplier-payments")
@RequiredArgsConstructor
@Tag(name = "Supplier payment rest api management controller")
public class SupplierPaymentController {

    private final SupplierPaymentService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SUPPLIER_PAYMENT_CREATE')")
    @Operation(
            summary = "Create supplier payment",
            description = "Only users with SUPPLIER_PAYMENT_CREATE permission can use it."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierPaymentResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody SupplierPaymentDTO dto) {

        SupplierPaymentResponse response = service.create(dto);

        return ResponseEntity.status(201).body(
                RestApiResponse.<SupplierPaymentResponse>builder()
                        .message("Supplier payment successfully created")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SUPPLIER_PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch all supplier payments",
            description = "Only users with SUPPLIER_PAYMENT_VIEW permission can use it."
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
                RestApiResponse.<Page<SupplierPaymentResponse>>builder()
                        .message("All supplier payments fetched successfully")
                        .data(service.findAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch supplier payment by id",
            description = "Only users with SUPPLIER_PAYMENT_VIEW permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierPaymentResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return ResponseEntity.ok(
                RestApiResponse.<SupplierPaymentResponse>builder()
                        .message("Supplier payment found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_PAYMENT_EDIT')")
    @Operation(
            summary = "Update supplier payment",
            description = "Only users with SUPPLIER_PAYMENT_EDIT permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierPaymentResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierPaymentDTO dto
    ) {

        SupplierPaymentResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<SupplierPaymentResponse>builder()
                        .message("Supplier payment successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_PAYMENT_DELETE')")
    @Operation(
            summary = "Delete supplier payment",
            description = "Only users with SUPPLIER_PAYMENT_DELETE permission can use it."
    )
    public ResponseEntity<?> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Supplier payment successfully deleted")
                        .build()
        );
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAuthority('SUPPLIER_PAYMENT_VIEW')")
    @Operation(
            summary = "Filter supplier payments",
            description = "Filter supplier payments by different parameters."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> filter(
            @RequestBody SupplierPaymentFilterDTO filter,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                RestApiResponse.<Page<SupplierPaymentResponse>>builder()
                        .message("Supplier payments filtered successfully")
                        .data(service.filter(filter, pageable))
                        .build()
        );
    }
}