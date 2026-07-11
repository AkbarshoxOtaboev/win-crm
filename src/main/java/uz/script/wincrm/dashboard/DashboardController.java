package uz.script.wincrm.dashboard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.script.wincrm.dashboard.DashboardService;
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard REST API", description = "Tizim dashboardi uchun savdo statistikasi (kunlik/haftalik/oylik sana oralig'i bo'yicha)")
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/top-goods/by-quantity")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(
            summary = "Eng ko'p sotilgan TOP 10 mahsulot (miqdor bo'yicha)",
            description = """
                    Berilgan sana oralig'ida (startDate - endDate) eng ko'p miqdorda sotilgan
                    TOP 10 mahsulotni qaytaradi.
                    
                    - Kunlik: startDate = endDate = bugungi sana
                    - Haftalik: startDate = shu haftaning dushanbasi, endDate = bugungi sana
                    - Oylik: startDate = shu oyning 1-kuni, endDate = bugungi sana
                    
                    Only users with DASHBOARD_VIEW permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TopGoodsResponse.class))
            )
    )
    public ResponseEntity<?> topGoodsByQuantity(
            @Parameter(description = "Start date", example = "2026-07-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", example = "2026-07-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<TopGoodsResponse>>builder()
                        .message("Eng ko'p sotilgan mahsulotlar (miqdor bo'yicha)")
                        .data(service.fetchTopGoodsByQuantity(startDate, endDate))
                        .build()
        );
    }

    @GetMapping("/top-goods/by-amount")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(
            summary = "Eng ko'p sotilgan TOP 10 mahsulot (summa bo'yicha)",
            description = """
                    Berilgan sana oralig'ida (startDate - endDate) eng ko'p summada sotilgan
                    TOP 10 mahsulotni qaytaradi.
                    
                    Only users with DASHBOARD_VIEW permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TopGoodsResponse.class))
            )
    )
    public ResponseEntity<?> topGoodsByAmount(
            @Parameter(description = "Start date", example = "2026-07-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", example = "2026-07-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<TopGoodsResponse>>builder()
                        .message("Eng ko'p sotilgan mahsulotlar (summa bo'yicha)")
                        .data(service.fetchTopGoodsByAmount(startDate, endDate))
                        .build()
        );
    }

    @GetMapping("/goods-group-summary")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(
            summary = "GoodsGroup bo'yicha jamlangan savdo statistikasi",
            description = """
                    Berilgan sana oralig'ida har bir mahsulot guruhi (GoodsGroup) bo'yicha
                    umumiy sotilgan miqdor va summani jamlab qaytaradi (summa bo'yicha kamayish tartibida).
                    
                    Only users with DASHBOARD_VIEW permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = GoodsGroupSummaryResponse.class))
            )
    )
    public ResponseEntity<?> goodsGroupSummary(
            @Parameter(description = "Start date", example = "2026-07-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", example = "2026-07-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<GoodsGroupSummaryResponse>>builder()
                        .message("Goods group bo'yicha statistika")
                        .data(service.fetchGoodsGroupSummary(startDate, endDate))
                        .build()
        );
    }
}