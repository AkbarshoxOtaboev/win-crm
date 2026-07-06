package uz.script.wincrm.goods.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "Unit Type ma'lumotlarini qaytarish uchun Response")
public class UnitTypeResponse {

    @Schema(
            description = "Unit Type ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "O'lchov birligi nomi",
            example = "Dona"
    )
    private String name;

    @Schema(
            description = "Holati",
            example = "ACTIVE"
    )
    private Status status;

    @Schema(
            description = "Yozuvni yaratgan foydalanuvchi",
            example = "admin"
    )
    private String createdUsername;

    @Schema(
            description = "Yaratilgan sana",
            example = "2026-07-06T15:10:30"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Oxirgi tahrirlangan sana",
            example = "2026-07-06T16:20:15"
    )
    private LocalDateTime updatedAt;
}