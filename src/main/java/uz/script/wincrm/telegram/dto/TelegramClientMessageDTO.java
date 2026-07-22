package uz.script.wincrm.telegram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Telegram Client Message DTO", description = "Tanlangan mijozga Telegram bot orqali xabar yuborish so'rovi")
public class TelegramClientMessageDTO {

    @NotNull(message = "clientId kiritilishi shart")
    @Schema(description = "Xabar yuboriladigan mijozning CRM'dagi id'si",
            example = "125", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clientId;

    @NotBlank(message = "Xabar matni kiritilishi shart")
    @Size(max = 4096, message = "Xabar matni 4096 belgidan oshmasligi kerak (Telegram cheklovi)")
    @Schema(description = "Mijozga yuboriladigan xabar matni",
            example = "Hurmatli mijoz, sizning qarzdorligingiz bo'yicha eslatma...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}