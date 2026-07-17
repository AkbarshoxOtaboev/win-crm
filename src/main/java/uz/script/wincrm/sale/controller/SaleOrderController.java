package uz.script.wincrm.sale.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.sale.dto.SaleOrderDTO;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
import uz.script.wincrm.sale.response.SaleOrderHistoryResponse;
import uz.script.wincrm.sale.response.SaleOrderResponse;
import uz.script.wincrm.sale.service.SaleOrderHistoryService;
import uz.script.wincrm.sale.service.SaleOrderService;
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sale-orders")
@RequiredArgsConstructor
@Tag(name = "Sale Order REST API", description = "Sale Order CRUD operations")
public class SaleOrderController {

    private final SaleOrderService service;
    private final SaleOrderHistoryService historyService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SALE_ORDER_CREATE')")
    @Operation(
            summary = "Create sale order",
            description = "Only users with SALE_ORDER_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody SaleOrderDTO dto) {
        SaleOrderResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<SaleOrderResponse>builder()
                                .message("Sale order successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch all sale orders",
            description = "Only users with SALE_ORDER_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderResponse>>builder()
                        .message("All sale orders fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch sale order by id",
            description = "Only users with SALE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<SaleOrderResponse>builder()
                        .message("Sale order found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch sale orders by client",
            description = "Only users with SALE_ORDER_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderResponse>>builder()
                        .message("Sale orders for client fetched successfully")
                        .data(service.fetchByClientId(clientId, pageable))
                        .build()
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch sale orders by warehouse",
            description = "Only users with SALE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByWarehouseId(
            @Parameter(description = "Warehouse ID", example = "1")
            @PathVariable Long warehouseId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<SaleOrderResponse>>builder()
                        .message("Sale orders for warehouse fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId, pageable))
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch sale orders by user",
            description = "Only users with SALE_ORDER_VIEW permission can use this endpoint. " +
                    "Returns sale orders placed by the given user (salesperson)."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByUserId(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<SaleOrderResponse>>builder()
                        .message("Sale orders for user fetched successfully")
                        .data(service.fetchByUserId(userId, pageable))
                        .build()
        );
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch sale orders by date range",
            description = "Only users with SALE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderResponse.class))
            )
    )
    public ResponseEntity<?> fetchByDateRange(
            @Parameter(description = "Start date", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderResponse>>builder()
                        .message("Sale orders for date range fetched successfully")
                        .data(service.fetchByDateRange(startDate, endDate))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_EDIT')")
    @Operation(
            summary = "Update sale order",
            description = "Only users with SALE_ORDER_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody SaleOrderDTO dto
    ) {
        SaleOrderResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<SaleOrderResponse>builder()
                        .message("Sale order successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_DELETE')")
    @Operation(
            summary = "Delete sale order",
            description = "Only users with SALE_ORDER_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Sale order successfully deleted")
                        .build()
        );
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SALE_ORDER_EDIT')")
    @Operation(
            summary = "Change sale order status",
            description = "Only users with SALE_ORDER_EDIT permission can change sale order status."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class)
            )
    )
    public ResponseEntity<?> changeStatus(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long id,

            @Parameter(
                    description = "New sale order status",
                    example = "CONFIRMED"
            )
            @RequestParam SalesOrderStatus salesOrderStatus
    ) {
        service.changeStatus(id, salesOrderStatus);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Sale order status successfully changed")
                        .build()
        );
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAuthority('SALE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch sale order status history",
            description = "Buyurtmaning barcha status o'zgarishlarini xronologik tartibda (eskisidan yangisiga) " +
                    "qaytaradi. Only users with SALE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderHistoryResponse.class))
            )
    )
    public ResponseEntity<?> fetchHistory(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderHistoryResponse>>builder()
                        .message("Sale order history fetched successfully")
                        .data(historyService.fetchBySaleOrderId(id))
                        .build()
        );
    }
}