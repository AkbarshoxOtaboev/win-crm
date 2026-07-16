package uz.script.wincrm.suppliers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.suppliers.dto.SupplierBalanceFilterDTO;
import uz.script.wincrm.suppliers.response.SupplierBalancePageResponse;
import uz.script.wincrm.suppliers.response.SupplierBalanceResponse;
import uz.script.wincrm.suppliers.service.SupplierBalanceService;
import uz.script.wincrm.utils.PageUtils;
import uz.script.wincrm.utils.RestApiResponse;
import uz.script.wincrm.utils.response.PageResponse;

@RestController
@RequestMapping("/api/supplier-balances")
@RequiredArgsConstructor
@Tag(name = "Supplier balance rest api management controller")
public class SupplierBalanceController {

    private final SupplierBalanceService service;

    @GetMapping("/{supplierId}")
    @PreAuthorize("hasAuthority('SUPPLIER_BALANCE_VIEW')")
    @Operation(
            summary = "Fetch supplier balance by supplier id",
            description = "Only users with SUPPLIER_BALANCE_VIEW permission can use it. " +
                    "Balance is calculated automatically from warehouse orders and payments."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierBalanceResponse.class)
            )
    )
    public ResponseEntity<?> findBySupplierId(@PathVariable Long supplierId) {

        return ResponseEntity.ok(
                RestApiResponse.<SupplierBalanceResponse>builder()
                        .message("Supplier balance found successfully")
                        .data(service.findBySupplierId(supplierId))
                        .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SUPPLIER_BALANCE_VIEW')")
    @Operation(
            summary = "Fetch all supplier balances",
            description = "Only users with SUPPLIER_BALANCE_VIEW permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierBalancePageResponse.class)
            )
    )
    public ResponseEntity<?> fetchAll(Pageable pageable) {
        return ResponseEntity.ok(
                RestApiResponse.<PageResponse<SupplierBalanceResponse>>builder()
                        .message("All supplier balances fetched successfully")
                        .data(PageUtils.from(service.findAll(pageable)))
                        .build()
        );
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAuthority('SUPPLIER_BALANCE_VIEW')")
    @Operation(
            summary = "Filter supplier balances",
            description = "Filter balances by supplier name and debt range."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierBalancePageResponse.class)
            )
    )
    public ResponseEntity<RestApiResponse<PageResponse<SupplierBalanceResponse>>> filter(
            @RequestBody SupplierBalanceFilterDTO filter,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                RestApiResponse.<PageResponse<SupplierBalanceResponse>>builder()
                        .message("Supplier balances filtered successfully")
                        .data(PageUtils.from(service.filter(filter, pageable)))
                        .build()
        );
    }
}