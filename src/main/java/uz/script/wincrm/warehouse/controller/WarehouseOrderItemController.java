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
import uz.script.wincrm.warehouse.dto.WarehouseOrderItemDTO;
import uz.script.wincrm.warehouse.response.WarehouseOrderItemResponse;
import uz.script.wincrm.warehouse.service.WarehouseOrderItemService;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse-order-items")
@RequiredArgsConstructor
@Tag(name = "Warehouse Order Item REST API Management Controller")
public class WarehouseOrderItemController {

    private final WarehouseOrderItemService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_CREATE')")
    @Operation(
            summary = "Create warehouse order item",
            description = "Only users with WAREHOUSE_ORDER_ITEM_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseOrderItemResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody WarehouseOrderItemDTO dto) {
        WarehouseOrderItemResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<WarehouseOrderItemResponse>builder()
                                .message("Warehouse order item successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch all warehouse order items",
            description = "Only users with WAREHOUSE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseOrderItemResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchAllItems() {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseOrderItemResponse>>builder()
                        .message("All warehouse order items fetched successfully")
                        .data(service.fetchAllItems())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch warehouse order item by id",
            description = "Only users with WAREHOUSE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseOrderItemResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<WarehouseOrderItemResponse>builder()
                        .message("Warehouse order item found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/by-order/{warehouseOrderId}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch warehouse order items by order",
            description = "Only users with WAREHOUSE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseOrderItemResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchByWarehouseOrderId(@PathVariable Long warehouseOrderId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseOrderItemResponse>>builder()
                        .message("Warehouse order items fetched successfully")
                        .data(service.fetchByWarehouseOrderId(warehouseOrderId))
                        .build()
        );
    }

    @GetMapping("/goods/{goodsId}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_VIEW')")
    @Operation(
            summary = "Fetch warehouse order items by goods",
            description = "Only users with WAREHOUSE_ORDER_ITEM_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseOrderItemResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchByGoodsId(@PathVariable Long goodsId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseOrderItemResponse>>builder()
                        .message("Warehouse order items fetched successfully")
                        .data(service.fetchByGoodsId(goodsId))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_EDIT')")
    @Operation(
            summary = "Update warehouse order item",
            description = "Only users with WAREHOUSE_ORDER_ITEM_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseOrderItemResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseOrderItemDTO dto
    ) {
        WarehouseOrderItemResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<WarehouseOrderItemResponse>builder()
                        .message("Warehouse order item successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_ITEM_DELETE')")
    @Operation(
            summary = "Delete warehouse order item",
            description = "Only users with WAREHOUSE_ORDER_ITEM_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Warehouse order item successfully deleted")
                        .build()
        );
    }
}