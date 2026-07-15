package uz.script.wincrm.telegram.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Bot Settings Response")
public class BotSettingsResponse {

    private Long id;

    private String botUsername;

    @Schema(description = "Token faqat oxirgi 4 belgisi bilan ko'rsatiladi", example = "****ueVa")
    private String maskedToken;

    private boolean active;

    private boolean botConnected;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}