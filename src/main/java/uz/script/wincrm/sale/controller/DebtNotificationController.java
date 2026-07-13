package uz.script.wincrm.sale.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.sale.response.DebtNotificationHistoryResponse;
import uz.script.wincrm.sale.response.DebtorClientResponse;
import uz.script.wincrm.sale.service.DebtNotificationService;
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/notifications/debt")
@RequiredArgsConstructor
@Tag(name = "Debt Notification REST API", description = "Admin panel orqali qarzdor mijozlarga SMS yuborish")
public class DebtNotificationController {

    private final DebtNotificationService service;

    @GetMapping("/debtors")
    @PreAuthorize("hasAuthority('DEBT_NOTIFICATION_VIEW')")
    @Operation(
            summary = "Qarzdor mijozlar ro'yxati (filtrlash bilan)",
            description = """
                    Admin panelda ko'rsatish uchun — qarzi bor mijozlar, ularning umumiy
                    qarz summasi va har bir buyurtmasi bo'yicha tafsilot.
                    
                    Barcha parametrlar ixtiyoriy:
                    - startDate / endDate: buyurtma sanasi (orderDate) oralig'i bo'yicha filtr
                    - userId: faqat shu xodimga (SaleOrder.user) biriktirilgan buyurtmalar
                    
                    Hech qanday parametr berilmasa, barcha qarzdorlar qaytariladi.
                    Faqat DEBT_NOTIFICATION_VIEW ruxsatiga ega foydalanuvchilar.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DebtorClientResponse.class))
            )
    )
    public ResponseEntity<?> fetchDebtors(
            @Parameter(description = "Sana oralig'i boshi (ixtiyoriy)", example = "2026-07-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Sana oralig'i oxiri (ixtiyoriy)", example = "2026-07-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Xodim (User) ID bo'yicha filtr (ixtiyoriy)", example = "3")
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<DebtorClientResponse>>builder()
                        .message("Qarzdor mijozlar ro'yxati")
                        .data(service.fetchDebtorClients(startDate, endDate, userId))
                        .build()
        );
    }

    @PostMapping("/send/client/{clientId}")
    @PreAuthorize("hasAuthority('DEBT_NOTIFICATION_CREATE')")
    @Operation(
            summary = "Bitta mijozga qarz haqida SMS yuborish",
            description = "Admin tanlagan bitta mijozga, uning barcha buyurtmalari bo'yicha umumiy qarz " +
                    "summasi haqida SMS yuboradi. Faqat DEBT_NOTIFICATION_SEND ruxsatiga ega foydalanuvchilar."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> sendToClient(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Long clientId
    ) {
        service.sendDebtNotificationToClient(clientId);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("SMS mijozga yuborildi")
                        .build()
        );
    }

    @PostMapping("/send/clients")
    @PreAuthorize("hasAuthority('DEBT_NOTIFICATION_CREATE')")
    @Operation(
            summary = "Tanlangan bir nechta mijozga qarz haqida SMS yuborish",
            description = "Admin panelda belgilab (checkbox bilan) tanlangan mijozlarning ID ro'yxati " +
                    "yuboriladi, har biriga alohida SMS jo'natiladi. Faqat DEBT_NOTIFICATION_SEND " +
                    "ruxsatiga ega foydalanuvchilar."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> sendToClients(@RequestBody List<Long> clientIds) {
        service.sendDebtNotificationsToClients(clientIds);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Tanlangan mijozlarga SMS yuborildi")
                        .build()
        );
    }

    @PostMapping("/send/order/{saleOrderId}")
    @PreAuthorize("hasAuthority('DEBT_NOTIFICATION_CREATE')")
    @Operation(
            summary = "Bitta buyurtma bo'yicha mijozga SMS yuborish",
            description = "Faqat DEBT_NOTIFICATION_SEND ruxsatiga ega foydalanuvchilar."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> sendForOrder(
            @Parameter(description = "Sale order ID", example = "1")
            @PathVariable Long saleOrderId
    ) {
        service.sendDebtNotificationForOrder(saleOrderId);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("SMS yuborildi")
                        .build()
        );
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('DEBT_NOTIFICATION_VIEW')")
    @Operation(
            summary = "Yuborilgan SMS'lar tarixi",
            description = "Barcha qarz haqidagi SMS'larning tarixi (muvaffaqiyatli va muvaffaqiyatsiz " +
                    "urinishlar bilan birga), sana bo'yicha eng yangisidan. " +
                    "Faqat DEBT_NOTIFICATION_VIEW ruxsatiga ega foydalanuvchilar."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchHistory(
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<DebtNotificationHistoryResponse>>builder()
                        .message("SMS tarixi")
                        .data(service.fetchHistory(pageable))
                        .build()
        );
    }

    @GetMapping("/history/client/{clientId}")
    @PreAuthorize("hasAuthority('DEBT_NOTIFICATION_VIEW')")
    @Operation(
            summary = "Bitta mijozga yuborilgan SMS'lar tarixi",
            description = "Faqat DEBT_NOTIFICATION_VIEW ruxsatiga ega foydalanuvchilar."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchHistoryByClientId(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Long clientId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<DebtNotificationHistoryResponse>>builder()
                        .message("Mijoz uchun SMS tarixi")
                        .data(service.fetchHistoryByClientId(clientId, pageable))
                        .build()
        );
    }
}