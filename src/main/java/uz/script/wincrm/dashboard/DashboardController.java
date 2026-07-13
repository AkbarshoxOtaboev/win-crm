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
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard REST API", description = "Tizim dashboardi uchun savdo va to'lov statistikasi")
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

    @GetMapping("/top-sellers")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(
            summary = "Eng ko'p savdo qilgan TOP 10 sotuvchi",
            description = """
                    Berilgan sana oralig'ida (startDate - endDate) eng ko'p umumiy savdo
                    summasiga ega bo'lgan TOP 10 sotuvchini (SaleOrder.user) qaytaradi.
                    Har bir qatorda buyurtmalar soni va umumiy savdo summasi ko'rsatiladi.
                    
                    Only users with DASHBOARD_VIEW permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TopSellerResponse.class))
            )
    )
    public ResponseEntity<?> topSellers(
            @Parameter(description = "Start date", example = "2026-07-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", example = "2026-07-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<TopSellerResponse>>builder()
                        .message("Eng ko'p savdo qilgan sotuvchilar")
                        .data(service.fetchTopSellers(startDate, endDate))
                        .build()
        );
    }

    @GetMapping("/payments/by-type")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(
            summary = "To'lovlarni PaymentType bo'yicha jamlash",
            description = """
                    Berilgan sana-vaqt oralig'ida (fromDate - toDate) barcha to'lovlarni
                    PaymentType (Naqd, Karta va h.k.) bo'yicha jamlab, har bir tur uchun
                    umumiy summa va to'lovlar sonini qaytaradi.
                    
                    Only users with DASHBOARD_VIEW permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PaymentTypeSummaryResponse.class))
            )
    )
    public ResponseEntity<?> paymentsByType(
            @Parameter(description = "Sana-vaqt oralig'i boshi", example = "2026-07-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "Sana-vaqt oralig'i oxiri", example = "2026-07-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<PaymentTypeSummaryResponse>>builder()
                        .message("To'lovlar PaymentType bo'yicha")
                        .data(service.fetchPaymentSummaryByType(fromDate, toDate))
                        .build()
        );
    }

    @GetMapping("/payments/daily")
    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @Operation(
            summary = "Kunlik to'lovlar (kun-ma-kun, PaymentType bo'yicha)",
            description = """
                    Berilgan sana-vaqt oralig'idagi HAR BIR KUN uchun alohida qator qaytaradi
                    (to'lov bo'lmagan kunlar ham 0 summa bilan ko'rsatiladi), har bir kun ichida
                    PaymentType bo'yicha taqsimot bilan.
                    
                    Only users with DASHBOARD_VIEW permission can use this endpoint.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DailyPaymentSummaryResponse.class))
            )
    )
    public ResponseEntity<?> dailyPayments(
            @Parameter(description = "Sana-vaqt oralig'i boshi", example = "2026-07-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @Parameter(description = "Sana-vaqt oralig'i oxiri", example = "2026-07-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<DailyPaymentSummaryResponse>>builder()
                        .message("Kunlik to'lovlar statistikasi")
                        .data(service.fetchDailyPayments(fromDate, toDate))
                        .build()
        );
    }
}