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
import uz.script.wincrm.sale.dto.SaleOrderItemDTO;
import uz.script.wincrm.sale.response.SaleOrderItemResponse;
import uz.script.wincrm.sale.service.SaleOrderItemService;
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sale-order-items")
@RequiredArgsConstructor
@Tag(name = "Sale Order Item REST API", description = "Sale Order Item CRUD operations with warehouse stock validation")
public class SaleOrderItemController {

    private final SaleOrderItemService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_CREATE')")
    @Operation(
            summary = "Create sale order item",
            description = """
                    ⭐ IMPORTANT: Warehouse stock validation is performed during creation!
                    
                    If warehouse has 10 items and you request 12:
                    - The request will be REJECTED
                    - Error will be returned to the client
                    - Sale order item will NOT be created
                    
                    Only users with SALE_ORDER_ITEM_CREATE permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderItemResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = """
                    Validation error or Insufficient stock!
                    
                    Example error response:
                    {
                      "error": "INSUFFICIENT_STOCK",
                      "message": "Ombareda yetarli mahsulot yo'q! Mavjud: 10, Talabalar: 12, Kamiy: 2"
                    }
                    """
    )
    public ResponseEntity<?> create(@Valid @RequestBody SaleOrderItemDTO dto) {
        SaleOrderItemResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<SaleOrderItemResponse>builder()
                                .message("Sale order item successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch all sale order items",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderItemResponse>>builder()
                        .message("All sale order items fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order item by id",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderItemResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Sale order item ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<SaleOrderItemResponse>builder()
                        .message("Sale order item found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order items by sale order id",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderItemResponse.class))
            )
    )
    public ResponseEntity<?> fetchBySaleOrderId(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderItemResponse>>builder()
                        .message("Sale order items fetched successfully")
                        .data(service.fetchBySaleOrderId(saleOrderId))
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}/paginated")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order items by sale order id (paginated)",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchBySaleOrderIdPaginated(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<SaleOrderItemResponse>>builder()
                        .message("Sale order items fetched successfully")
                        .data(service.fetchBySaleOrderIdPaginated(saleOrderId, pageable))
                        .build()
        );
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order items by client id",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderItemResponse>>builder()
                        .message("Sale order items for client fetched successfully")
                        .data(service.fetchByClientId(clientId, pageable))
                        .build()
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order items by warehouse id",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderItemResponse>>builder()
                        .message("Sale order items for warehouse fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId, pageable))
                        .build()
        );
    }

    @GetMapping("/goods/{goodsId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order items by goods id",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByGoodsId(
            @Parameter(description = "Goods ID", example = "1")
            @PathVariable Long goodsId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<SaleOrderItemResponse>>builder()
                        .message("Sale order items for goods fetched successfully")
                        .data(service.fetchByGoodsId(goodsId, pageable))
                        .build()
        );
    }

    @GetMapping("/arrival-date-range")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch sale order items by arrival date range",
            description = "Only users with SALE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderItemResponse.class))
            )
    )
    public ResponseEntity<?> fetchByArrivalDateRange(
            @Parameter(description = "Start date", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderItemResponse>>builder()
                        .message("Sale order items for arrival date range fetched successfully")
                        .data(service.fetchByArrivalDateRange(startDate, endDate))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_EDIT')")
    @Operation(
            summary = "Update sale order item",
            description = """
                    ⭐ IMPORTANT: If count is changed, warehouse stock validation is performed again!
                    
                    Only users with SALE_ORDER_ITEM_EDIT permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderItemResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Validation error or Insufficient stock!"
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Sale order item ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody SaleOrderItemDTO dto
    ) {
        SaleOrderItemResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<SaleOrderItemResponse>builder()
                        .message("Sale order item successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_ITEM_DELETE')")
    @Operation(
            summary = "Delete sale order item",
            description = "Only users with SALE_ORDER_ITEM_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Sale order item ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Sale order item successfully deleted")
                        .build()
        );
    }
}