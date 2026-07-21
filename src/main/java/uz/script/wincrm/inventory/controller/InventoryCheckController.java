package uz.script.wincrm.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.inventory.request.StartInventoryCheckRequest;
import uz.script.wincrm.inventory.request.UpdateInventoryCheckItemRequest;
import uz.script.wincrm.inventory.response.InventoryCheckResponse;
import uz.script.wincrm.inventory.service.InventoryCheckService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-checks")
@RequiredArgsConstructor
@Tag(name = "Inventory Check REST API Management Controller")
public class InventoryCheckController {

    private final InventoryCheckService service;

    @GetMapping
    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @Operation(
            summary = "Fetch all inventory checks",
            description = "Only users with INVENTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryCheckResponse.class))
            )
    )
    public ResponseEntity<?> fetchAll() {
        return ResponseEntity.ok(
                RestApiResponse.<List<InventoryCheckResponse>>builder()
                        .message("All inventory checks fetched successfully")
                        .data(service.fetchAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @Operation(
            summary = "Fetch inventory check by id",
            description = "Only users with INVENTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryCheckResponse.class))
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<InventoryCheckResponse>builder()
                        .message("Inventory check found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @Operation(
            summary = "Fetch inventory checks by warehouse",
            description = "Only users with INVENTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = InventoryCheckResponse.class))
            )
    )
    public ResponseEntity<?> fetchByWarehouseId(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<InventoryCheckResponse>>builder()
                        .message("Inventory checks fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId))
                        .build()
        );
    }

    @PostMapping("/start")
    @PreAuthorize("hasAuthority('INVENTORY_CREATE')")
    @Operation(
            summary = "Start a new inventory check",
            description = "Only users with INVENTORY_CREATE permission can use this endpoint. " +
                    "Tanlangan ombordagi joriy Stock qoldiqlarini muzlatib, yangi inventarizatsiya boshlaydi."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryCheckResponse.class))
    )
    public ResponseEntity<?> start(@Valid @RequestBody StartInventoryCheckRequest request) {
        return ResponseEntity.ok(
                RestApiResponse.<InventoryCheckResponse>builder()
                        .message("Inventory check started successfully")
                        .data(service.start(request))
                        .build()
        );
    }

    @PutMapping("/{inventoryCheckId}/items/{itemId}")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    @Operation(
            summary = "Update actual count for an inventory check item",
            description = "Only users with INVENTORY_UPDATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryCheckResponse.class))
    )
    public ResponseEntity<?> updateItem(
            @PathVariable Long inventoryCheckId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateInventoryCheckItemRequest request) {
        return ResponseEntity.ok(
                RestApiResponse.<InventoryCheckResponse>builder()
                        .message("Inventory check item updated successfully")
                        .data(service.updateItem(inventoryCheckId, itemId, request))
                        .build()
        );
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    @Operation(
            summary = "Confirm an inventory check",
            description = "Only users with INVENTORY_UPDATE permission can use this endpoint. " +
                    "Farqlarni Stockka qo'llaydi va StockHistory yozadi."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryCheckResponse.class))
    )
    public ResponseEntity<?> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<InventoryCheckResponse>builder()
                        .message("Inventory check confirmed successfully")
                        .data(service.confirm(id))
                        .build()
        );
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    @Operation(
            summary = "Cancel an inventory check",
            description = "Only users with INVENTORY_UPDATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryCheckResponse.class))
    )
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<InventoryCheckResponse>builder()
                        .message("Inventory check cancelled successfully")
                        .data(service.cancel(id))
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_DELETE')")
    @Operation(
            summary = "Delete inventory check",
            description = "Only users with INVENTORY_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Inventory check successfully deleted")
                        .build()
        );
    }
}