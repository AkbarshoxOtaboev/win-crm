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
import uz.script.wincrm.payment.dto.PaymentDTO;
import uz.script.wincrm.payment.response.PaymentResponse;
import uz.script.wincrm.payment.service.PaymentService;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment REST API", description = "Payment CRUD operations")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PAYMENT_CREATE')")
    @Operation(
            summary = "Create payment",
            description = "Only users with PAYMENT_CREATE permission can use this endpoint. " +
                    "clientId is required. saleOrderId is optional — omit it for a general/advance payment " +
                    "not tied to a specific order. If saleOrderId is provided, the related sale order's " +
                    "paidSum and debtSum are automatically recalculated."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody PaymentDTO dto) {
        PaymentResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<PaymentResponse>builder()
                                .message("Payment successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch all payments",
            description = "Only users with PAYMENT_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<PaymentResponse>>builder()
                        .message("All payments fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch payment by id",
            description = "Only users with PAYMENT_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Payment ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<PaymentResponse>builder()
                        .message("Payment found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch payments by client",
            description = "Only users with PAYMENT_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByClientId(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Long clientId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<PaymentResponse>>builder()
                        .message("Payments for client fetched successfully")
                        .data(service.fetchByClientId(clientId, pageable))
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch payments by sale order",
            description = "Only users with PAYMENT_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchBySaleOrderId(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<PaymentResponse>>builder()
                        .message("Payments for sale order fetched successfully")
                        .data(service.fetchBySaleOrderId(saleOrderId, pageable))
                        .build()
        );
    }

    @GetMapping("/payment-type/{paymentTypeId}")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(
            summary = "Fetch payments by payment type",
            description = "Only users with PAYMENT_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByPaymentTypeId(
            @Parameter(description = "Payment type ID", example = "1")
            @PathVariable Long paymentTypeId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<PaymentResponse>>builder()
                        .message("Payments for payment type fetched successfully")
                        .data(service.fetchByPaymentTypeId(paymentTypeId, pageable))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_EDIT')")
    @Operation(
            summary = "Update payment",
            description = "Only users with PAYMENT_EDIT permission can use this endpoint. " +
                    "Automatically recalculates the related sale order's paidSum and debtSum."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Payment ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PaymentDTO dto
    ) {
        PaymentResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<PaymentResponse>builder()
                        .message("Payment successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_DELETE')")
    @Operation(
            summary = "Delete payment",
            description = "Only users with PAYMENT_DELETE permission can use this endpoint. " +
                    "Automatically recalculates the related sale order's paidSum and debtSum."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Payment ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Payment successfully deleted")
                        .build()
        );
    }
}