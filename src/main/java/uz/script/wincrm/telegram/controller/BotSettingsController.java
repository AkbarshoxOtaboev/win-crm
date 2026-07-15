package uz.script.wincrm.telegram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.telegram.config.TelegramBotLifecycleService;
import uz.script.wincrm.telegram.dto.BotSettingsDTO;
import uz.script.wincrm.telegram.response.BotSettingsResponse;
import uz.script.wincrm.telegram.service.BotSettingsService;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/telegram/bot-settings")
@RequiredArgsConstructor
@Tag(name = "Telegram Bot Settings REST API Management Controller")
public class BotSettingsController {

    private final BotSettingsService service;
    private final TelegramBotLifecycleService lifecycleService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('BOT_SETTINGS_CREATE')")
    @Operation(
            summary = "Telegram bot tokenini registratsiya qilish / yangilash",
            description = "Only users with BOT_SETTINGS_MANAGE permission can use this endpoint. " +
                    "Token BotFather'dan olinadi, bazada shifrlangan holda saqlanadi va bot " +
                    "shu zahoti (ilovani qayta ishga tushirmasdan) Telegram bilan ulanadi."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BotSettingsResponse.class))
    )
    public ResponseEntity<?> register(@Valid @RequestBody BotSettingsDTO dto) {
        BotSettingsResponse response = service.registerOrUpdate(dto);

        // Token saqlangandan so'ng botni darhol qayta ulaymiz — restart shart emas.
        lifecycleService.startBot();

        response = service.getActiveSettings();

        return ResponseEntity.ok(
                RestApiResponse.<BotSettingsResponse>builder()
                        .message(response.isBotConnected()
                                ? "Bot token saqlandi va bot muvaffaqiyatli ulandi."
                                : "Bot token saqlandi, lekin ulanishda xatolik yuz berdi. " +
                                "Tokenni tekshiring va /reconnect orqali qayta urinib ko'ring.")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/reconnect")
    @PreAuthorize("hasAuthority('BOT_SETTINGS_MANAGE')")
    @Operation(
            summary = "Botni qo'lda qayta ulash",
            description = "Only users with BOT_SETTINGS_MANAGE permission can use this endpoint. " +
                    "Tarmoq uzilishi yoki oldingi ulanish xatoligidan so'ng botni qayta ulash uchun."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> reconnect() {
        lifecycleService.startBot();

        return ResponseEntity.ok(
                RestApiResponse.<BotSettingsResponse>builder()
                        .message("Qayta ulanish urinildi")
                        .data(service.getActiveSettings())
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BOT_SETTINGS_VIEW')")
    @Operation(
            summary = "Faol bot sozlamalarini olish",
            description = "Only users with BOT_SETTINGS_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BotSettingsResponse.class))
    )
    public ResponseEntity<?> getActive() {
        return ResponseEntity.ok(
                RestApiResponse.<BotSettingsResponse>builder()
                        .message("Bot sozlamalari topildi")
                        .data(service.getActiveSettings())
                        .build()
        );
    }
}