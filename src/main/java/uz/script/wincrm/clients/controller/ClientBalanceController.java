package uz.script.wincrm.clients.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.clients.dto.ClientBalanceDTO;
import uz.script.wincrm.clients.response.ClientBalanceResponse;
import uz.script.wincrm.clients.service.ClientBalanceService;
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/client-balances")
@RequiredArgsConstructor
@Tag(name = "Client Balance REST API Management Controller")
public class ClientBalanceController {

    private final ClientBalanceService service;

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENT_BALANCE_VIEW')")
    @Operation(
            summary = "Fetch all client balances",
            description = "Har bir mijoz uchun totalPurchase/totalPaid/totalDebt ko'rsatilgan davr bo'yicha " +
                    "hisoblanadi. fromDate/toDate berilmasa, joriy oy (1-kunidan oxirigacha) olinadi. " +
                    "Only users with CLIENT_BALANCE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ClientBalanceResponse.class))
            )
    )
    public ResponseEntity<?> fetchAll(
            @Parameter(description = "Davr boshlanishi, masalan 2026-07-01. Berilmasa - joriy oyning 1-kuni")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Davr oxiri, masalan 2026-07-31. Berilmasa - joriy oyning oxirgi kuni")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<ClientBalanceResponse>>builder()
                        .message("All client balances fetched successfully")
                        .data(service.fetchAll(fromDate, toDate))
                        .build()
        );
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAuthority('CLIENT_BALANCE_VIEW')")
    @Operation(
            summary = "Fetch client balance by client id",
            description = "totalPurchase/totalPaid/totalDebt ko'rsatilgan davr bo'yicha hisoblanadi. " +
                    "fromDate/toDate berilmasa, joriy oy (1-kunidan oxirigacha) olinadi. " +
                    "Only users with CLIENT_BALANCE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientBalanceResponse.class)
            )
    )
    public ResponseEntity<?> findByClientId(
            @PathVariable Long clientId,
            @Parameter(description = "Davr boshlanishi, masalan 2026-07-01. Berilmasa - joriy oyning 1-kuni")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Davr oxiri, masalan 2026-07-31. Berilmasa - joriy oyning oxirgi kuni")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<ClientBalanceResponse>builder()
                        .message("Client balance found successfully")
                        .data(service.findByClientId(clientId, fromDate, toDate))
                        .build()
        );
    }

    @PutMapping("/recalculate/{clientId}")
    @PreAuthorize("hasAuthority('CLIENT_BALANCE_EDIT')")
    @Operation(
            summary = "Recalculate client balance",
            description = "Sale order va payment yozuvlaridan totalPurchase, totalPaid, totalDebt qiymatlarini qayta hisoblaydi. Only users with CLIENT_BALANCE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientBalanceResponse.class)
            )
    )
    public ResponseEntity<?> recalculate(@PathVariable Long clientId) {
        service.recalculateClientBalance(clientId);

        // recalculateClientBalance() har doim barcha vaqt (all-time) balansini qayta hisoblaydi,
        // shu sababli natija ham findByClientId (joriy oy) emas, findAllTimeByClientId orqali qaytariladi.
        return ResponseEntity.ok(
                RestApiResponse.<ClientBalanceResponse>builder()
                        .message("Client balance recalculated successfully")
                        .data(service.findAllTimeByClientId(clientId))
                        .build()
        );
    }

    @PutMapping("/adjust/{clientId}")
    @PreAuthorize("hasAuthority('CLIENT_BALANCE_EDIT')")
    @Operation(
            summary = "Manually adjust client balance",
            description = "Avtomatik hisoblangan balansni qo'lda ustidan yozish uchun (faqat zarurat bo'lganda ishlating). Only users with CLIENT_BALANCE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientBalanceResponse.class)
            )
    )
    public ResponseEntity<?> adjust(
            @PathVariable Long clientId,
            @Valid @RequestBody ClientBalanceDTO dto
    ) {
        ClientBalanceResponse response = service.adjustBalance(clientId, dto);

        return ResponseEntity.ok(
                RestApiResponse.<ClientBalanceResponse>builder()
                        .message("Client balance adjusted successfully")
                        .data(response)
                        .build()
        );
    }
}