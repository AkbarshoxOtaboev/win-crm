package uz.script.wincrm.telegram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.script.wincrm.telegram.dto.TelegramClientMessageDTO;
import uz.script.wincrm.telegram.service.TelegramMessageService;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/telegram/messages")
@RequiredArgsConstructor
@Tag(name = "Telegram Client Messaging REST API Controller")
public class TelegramMessageController {

    private final TelegramMessageService service;

    @PostMapping("/send-to-client")
    @PreAuthorize("hasAuthority('BOT_SETTINGS_CREATE')")
    @Operation(
            summary = "Tanlangan mijozga Telegram bot orqali xabar yuborish",
            description = "Only users with BOT_SETTING_CREATE permission can use this endpoint. " +
                    "Mijoz botdan avval telefon raqami orqali ro'yxatdan o'tgan bo'lishi shart, " +
                    "aks holda xatolik qaytariladi."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> sendToClient(@Valid @RequestBody TelegramClientMessageDTO dto) {
        service.sendToClient(dto);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Xabar mijozga muvaffaqiyatli yuborildi.")
                        .build()
        );
    }
}