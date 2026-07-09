package uz.script.wincrm.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.payment.dto.PaymentTypeDTO;
import uz.script.wincrm.payment.response.PaymentTypeResponse;
import uz.script.wincrm.payment.service.PaymentTypeService;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/payment-types")
@RequiredArgsConstructor
@Tag(name = "Payment Type REST API", description = "Payment Type CRUD operations")
public class PaymentTypeController {

    private final PaymentTypeService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PAYMENT_TYPE_CREATE')")
    @Operation(
            summary = "Create payment type",
            description = "Only users with PAYMENT_TYPE_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentTypeResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody PaymentTypeDTO dto) {
        PaymentTypeResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<PaymentTypeResponse>builder()
                                .message("Payment type successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_TYPE_VIEW')")
    @Operation(
            summary = "Fetch all payment types",
            description = "Only users with PAYMENT_TYPE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchAll(
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<PaymentTypeResponse>>builder()
                        .message("All payment types fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_TYPE_VIEW')")
    @Operation(
            summary = "Fetch payment type by id",
            description = "Only users with PAYMENT_TYPE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentTypeResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Payment type ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<PaymentTypeResponse>builder()
                        .message("Payment type found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_TYPE_EDIT')")
    @Operation(
            summary = "Update payment type",
            description = "Only users with PAYMENT_TYPE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentTypeResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Payment type ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PaymentTypeDTO dto
    ) {
        PaymentTypeResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<PaymentTypeResponse>builder()
                        .message("Payment type successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_TYPE_DELETE')")
    @Operation(
            summary = "Delete payment type",
            description = "Only users with PAYMENT_TYPE_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Payment type ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Payment type successfully deleted")
                        .build()
        );
    }
}