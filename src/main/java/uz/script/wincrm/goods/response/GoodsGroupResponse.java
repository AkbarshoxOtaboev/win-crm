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
@Schema(description = "Goods Group ma'lumotlarini qaytarish uchun Response")
public class GoodsGroupResponse {

    @Schema(
            description = "Goods Group ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Tovar guruhi nomi",
            example = "Ichimliklar"
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
            example = "2026-07-06T15:20:30"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Oxirgi tahrirlangan sana",
            example = "2026-07-06T16:45:10"
    )
    private LocalDateTime updatedAt;
}