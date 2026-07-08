package uz.script.wincrm.stock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.stock.response.StockResponse;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Tag(name = "Stock REST API Management Controller")
public class StockController {

    private final StockService service;

    @GetMapping
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch all stocks",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockResponse.class))
            )
    )
    public ResponseEntity<?> fetchAll() {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockResponse>>builder()
                        .message("All stocks fetched successfully")
                        .data(service.fetchAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch stock by id",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockResponse.class))
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<StockResponse>builder()
                        .message("Stock found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch stocks by warehouse",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockResponse.class))
            )
    )
    public ResponseEntity<?> fetchByWarehouseId(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockResponse>>builder()
                        .message("Stocks fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId))
                        .build()
        );
    }

    @GetMapping("/by-goods/{goodsId}")
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch stocks by goods",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockResponse.class))
            )
    )
    public ResponseEntity<?> fetchByGoodsId(@PathVariable Long goodsId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockResponse>>builder()
                        .message("Stocks fetched successfully")
                        .data(service.fetchByGoodsId(goodsId))
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('STOCK_DELETE')")
    @Operation(
            summary = "Delete stock",
            description = "Only users with STOCK_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Stock successfully deleted")
                        .build()
        );
    }
}