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
import uz.script.wincrm.warehouse.dto.WarehouseOrderDTO;
import uz.script.wincrm.warehouse.response.WarehouseOrderResponse;
import uz.script.wincrm.warehouse.service.WarehouseOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse-orders")
@RequiredArgsConstructor
@Tag(name = "Warehouse Order REST API Management Controller")
public class WarehouseOrderController {

    private final WarehouseOrderService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_CREATE')")
    @Operation(
            summary = "Create warehouse order",
            description = "Only users with WAREHOUSE_ORDER_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseOrderResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody WarehouseOrderDTO dto) {
        WarehouseOrderResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<WarehouseOrderResponse>builder()
                                .message("Warehouse order successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch all warehouse orders",
            description = "Only users with WAREHOUSE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseOrderResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchAllOrders() {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseOrderResponse>>builder()
                        .message("All warehouse orders fetched successfully")
                        .data(service.fetchAllOrders())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch warehouse order by id",
            description = "Only users with WAREHOUSE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseOrderResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<WarehouseOrderResponse>builder()
                        .message("Warehouse order found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch warehouse orders by warehouse",
            description = "Only users with WAREHOUSE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseOrderResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchByWarehouseId(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseOrderResponse>>builder()
                        .message("Warehouse orders fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId))
                        .build()
        );
    }

    @GetMapping("/by-supplier/{supplierId}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_VIEW')")
    @Operation(
            summary = "Fetch warehouse orders by supplier",
            description = "Only users with WAREHOUSE_ORDER_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = WarehouseOrderResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<WarehouseOrderResponse>>builder()
                        .message("Warehouse orders fetched successfully")
                        .data(service.fetchBySupplierId(supplierId))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_EDIT')")
    @Operation(
            summary = "Update warehouse order",
            description = "Only users with WAREHOUSE_ORDER_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WarehouseOrderResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseOrderDTO dto
    ) {
        WarehouseOrderResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<WarehouseOrderResponse>builder()
                        .message("Warehouse order successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_DELETE')")
    @Operation(
            summary = "Delete warehouse order",
            description = "Only users with WAREHOUSE_ORDER_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Warehouse order successfully deleted")
                        .build()
        );
    }

    @PatchMapping("/{id}/transfer")
    @PreAuthorize("hasAuthority('WAREHOUSE_ORDER_EDIT')")
    @Operation(summary = "Transfer warehouse order to stock",
            description = "Order statusini TRANSFERRED qiladi va item'larni Stock'ga qo'shadi.")
    public ResponseEntity<?> transferToWarehouse(@PathVariable Long id) {
        WarehouseOrderResponse response = service.transferToWarehouse(id);
        return ResponseEntity.ok(
                RestApiResponse.<WarehouseOrderResponse>builder()
                        .message("Warehouse order omborga muvaffaqiyatli transfer qilindi")
                        .data(response)
                        .build()
        );
    }
}
