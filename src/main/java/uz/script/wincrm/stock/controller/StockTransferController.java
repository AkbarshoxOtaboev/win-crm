package uz.script.wincrm.stock.controller;

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
import uz.script.wincrm.stock.request.StockTransferRequest;
import uz.script.wincrm.stock.response.StockTransferResponse;
import uz.script.wincrm.stock.service.StockTransferService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transfers")
@RequiredArgsConstructor
@Tag(name = "Stock Transfer REST API Management Controller")
public class StockTransferController {

    private final StockTransferService service;

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_EDIT')")
    @Operation(
            summary = "Ombordan omborga mahsulot ko'chirish",
            description = "Only users with STOCK_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockTransferResponse.class))
    )
    public ResponseEntity<?> transfer(@Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.ok(
                RestApiResponse.<StockTransferResponse>builder()
                        .message("Stock successfully transferred")
                        .data(service.transfer(request))
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch all stock transfers",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockTransferResponse.class))
            )
    )
    public ResponseEntity<?> fetchAll() {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockTransferResponse>>builder()
                        .message("All stock transfers fetched successfully")
                        .data(service.fetchAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch stock transfer by id",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockTransferResponse.class))
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<StockTransferResponse>builder()
                        .message("Stock transfer found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/by-warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch stock transfers by warehouse",
            description = "Ombor manba yoki maqsad sifatida ishtirok etgan barcha transferlarni qaytaradi. Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockTransferResponse.class))
            )
    )
    public ResponseEntity<?> fetchByWarehouseId(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockTransferResponse>>builder()
                        .message("Stock transfers fetched successfully")
                        .data(service.fetchByWarehouseId(warehouseId))
                        .build()
        );
    }

    @GetMapping("/by-goods/{goodsId}")
    @PreAuthorize("hasAuthority('STOCK_VIEW')")
    @Operation(
            summary = "Fetch stock transfers by goods",
            description = "Only users with STOCK_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StockTransferResponse.class))
            )
    )
    public ResponseEntity<?> fetchByGoodsId(@PathVariable Long goodsId) {
        return ResponseEntity.ok(
                RestApiResponse.<List<StockTransferResponse>>builder()
                        .message("Stock transfers fetched successfully")
                        .data(service.fetchByGoodsId(goodsId))
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('STOCK_DELETE')")
    @Operation(
            summary = "Delete stock transfer record",
            description = "Faqat transfer tarixi yozuvini soft-delete qiladi, ombordagi count'larni qaytarmaydi. Only users with STOCK_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Stock transfer successfully deleted")
                        .build()
        );
    }
}
