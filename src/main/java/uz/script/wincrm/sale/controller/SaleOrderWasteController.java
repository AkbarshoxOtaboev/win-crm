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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.sale.dto.SaleOrderWasteDTO;
import uz.script.wincrm.sale.response.SaleOrderWasteResponse;
import uz.script.wincrm.sale.response.SaleOrderWasteSummaryResponse;
import uz.script.wincrm.sale.service.SaleOrderWasteService;
import uz.script.wincrm.utils.RestApiResponse;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sale-order-wastes")
@RequiredArgsConstructor
@Tag(
        name = "Sale Order Waste REST API",
        description = "Buyurtma bo'yicha ishlab chiqarishdan ortib qolgan materiallarni qayd etish " +
                "(faqat info, hech narsaga ta'sir qilmaydi)"
)
public class SaleOrderWasteController {

    private final SaleOrderWasteService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_CREATE')")
    @Operation(
            summary = "Record sale order waste",
            description = "Ishlab chiqarishdan ortib qolgan materialni qayd etadi. Faqat info sifatida " +
                    "saqlanadi - stock, totalSum yoki debtSum'ga hech qanday ta'sir qilmaydi. " +
                    "Only users with SALE_ORDER_WASTE_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderWasteResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody SaleOrderWasteDTO dto) {
        SaleOrderWasteResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<SaleOrderWasteResponse>builder()
                                .message("Sale order waste successfully recorded")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch all sale order wastes",
            description = "Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderWasteResponse>>builder()
                        .message("All sale order wastes fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch sale order waste by id",
            description = "Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderWasteResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Sale order waste ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<SaleOrderWasteResponse>builder()
                        .message("Sale order waste found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch wastes by sale order",
            description = "Berilgan buyurtma bo'yicha barcha ortib qolgan material yozuvlarini qaytaradi. " +
                    "Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderWasteResponse.class))
            )
    )
    public ResponseEntity<?> fetchBySaleOrderId(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderWasteResponse>>builder()
                        .message("Sale order wastes fetched successfully")
                        .data(service.fetchBySaleOrderId(saleOrderId))
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}/paged")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch wastes by sale order (paginated)",
            description = "Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderWasteResponse>>builder()
                        .message("Sale order wastes fetched successfully")
                        .data(service.fetchBySaleOrderIdPaginated(saleOrderId, pageable))
                        .build()
        );
    }

    @GetMapping("/goods/{goodsId}")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch wastes by goods",
            description = "Berilgan material (Goods) bo'yicha barcha buyurtmalardagi ortib qolgan yozuvlarni " +
                    "qaytaradi. Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<SaleOrderWasteResponse>>builder()
                        .message("Sale order wastes fetched successfully")
                        .data(service.fetchByGoodsId(goodsId, pageable))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_EDIT')")
    @Operation(
            summary = "Update sale order waste",
            description = "Only users with SALE_ORDER_WASTE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleOrderWasteResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Sale order waste ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody SaleOrderWasteDTO dto
    ) {
        SaleOrderWasteResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<SaleOrderWasteResponse>builder()
                        .message("Sale order waste successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_DELETE')")
    @Operation(
            summary = "Delete sale order waste",
            description = "Only users with SALE_ORDER_WASTE_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Sale order waste ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Sale order waste successfully deleted")
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}/total")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Calculate total waste for a sale order",
            description = "Berilgan buyurtma bo'yicha jami ortib qolgan material miqdorini (barcha materiallar " +
                    "yig'indisi) hisoblab qaytaradi. Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BigDecimal.class)
            )
    )
    public ResponseEntity<?> calculateTotalBySaleOrderId(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<BigDecimal>builder()
                        .message("Total waste quantity calculated successfully")
                        .data(service.calculateTotalBySaleOrderId(saleOrderId))
                        .build()
        );
    }

    @GetMapping("/sale-order/{saleOrderId}/summary")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch waste summary by goods for a sale order",
            description = "Berilgan buyurtma bo'yicha material (Goods) kesimida jamlangan ortib qolgan " +
                    "miqdorlarni qaytaradi. Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderWasteSummaryResponse.class))
            )
    )
    public ResponseEntity<?> fetchSummaryBySaleOrderId(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderWasteSummaryResponse>>builder()
                        .message("Sale order waste summary fetched successfully")
                        .data(service.fetchSummaryBySaleOrderId(saleOrderId))
                        .build()
        );
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('SALE_ORDER_WASTE_VIEW')")
    @Operation(
            summary = "Fetch overall waste summary grouped by goods",
            description = "Barcha buyurtmalar bo'yicha material turi kesimida jamlangan umumiy ortib qolgan " +
                    "miqdorlarni qaytaradi - qayta ishlatish uchun yetarli material to'planganini aniqlashda foydali. " +
                    "Only users with SALE_ORDER_WASTE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SaleOrderWasteSummaryResponse.class))
            )
    )
    public ResponseEntity<?> fetchSummaryGroupedByGoods() {
        return ResponseEntity.ok(
                RestApiResponse.<List<SaleOrderWasteSummaryResponse>>builder()
                        .message("Sale order waste summary fetched successfully")
                        .data(service.fetchSummaryGroupedByGoods())
                        .build()
        );
    }
}