package uz.script.wincrm.telegram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Bot Settings DTO", description = "Telegram bot token registratsiyasi / yangilash so'rovi")
public class BotSettingsDTO {

    @NotBlank(message = "Bot username kiritilishi shart")
    @Schema(description = "BotFather'da yaratilgan bot username",
            example = "@WinCrmBot", requiredMode = Schema.RequiredMode.REQUIRED)
    private String botUsername;

    @NotBlank(message = "Bot token kiritilishi shart")
    @Schema(description = "BotFather bergan token (bazada shifrlangan holda saqlanadi)",
            example = "123456789:AAExampleTokenValue", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}