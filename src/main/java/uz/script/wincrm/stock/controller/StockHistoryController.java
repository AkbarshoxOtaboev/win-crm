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
import uz.script.wincrm.stock.response.StockHistoryResponse;
import uz.script.wincrm.stock.service.StockHistoryService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/stock-histories")
@RequiredArgsConstructor
@Tag(name = "Stock History REST API Management Controller")
public class StockHistoryController {

    private final StockHistoryService service;

    @GetMapping
    @PreAuthorize("hasAuthority('STOCK_HISTORY_VIEW')")
    @Operation(
            summary = "Fetch all stock histories",
            description = "Only users with STOCK_HISTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockHistoryResponse.class))
            )
    )
    public ResponseEntity<?> fetchAll() {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockHistoryResponse>>builder()
                        .message("All stock histories fetched successfully")
                        .data(service.fetchAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STOCK_HISTORY_VIEW')")
    @Operation(
            summary = "Fetch stock history by id",
            description = "Only users with STOCK_HISTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockHistoryResponse.class))
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<StockHistoryResponse>builder()
                        .message("Stock history found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('STOCK_HISTORY_VIEW')")
    @Operation(
            summary = "Fetch stock histories by warehouse",
            description = "Only users with STOCK_HISTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockHistoryResponse.class))
            )
    )
    public ResponseEntity<?> fetchByWarehouseId(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockHistoryResponse>>builder()
                        .message("Stock histories fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId))
                        .build()
        );
    }

    @GetMapping("/by-goods/{goodsId}")
    @PreAuthorize("hasAuthority('STOCK_HISTORY_VIEW')")
    @Operation(
            summary = "Fetch stock histories by goods",
            description = "Only users with STOCK_HISTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockHistoryResponse.class))
            )
    )
    public ResponseEntity<?> fetchByGoodsId(@PathVariable Long goodsId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockHistoryResponse>>builder()
                        .message("Stock histories fetched successfully")
                        .data(service.fetchByGoodsId(goodsId))
                        .build()
        );
    }

    @GetMapping("/by-goods/{goodsId}/warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('STOCK_HISTORY_VIEW')")
    @Operation(
            summary = "Fetch stock histories by goods and warehouse",
            description = "Only users with STOCK_HISTORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockHistoryResponse.class))
            )
    )
    public ResponseEntity<?> fetchByGoodsIdAndWarehouseId(
            @PathVariable Long goodsId,
            @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockHistoryResponse>>builder()
                        .message("Stock histories fetched successfully")
                        .data(service.fetchByGoodsIdAndWarehouseId(goodsId, warehouseId))
                        .build()
        );
    }
}