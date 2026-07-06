package uz.script.wincrm.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Goods Group yaratish va tahrirlash uchun DTO")
public class GoodsGroupDTO {

    @NotBlank(message = "Guruh nomi bo'sh bo'lishi mumkin emas")
    @Size(max = 100, message = "Guruh nomi 100 ta belgidan oshmasligi kerak")
    @Schema(
            description = "Tovar guruhi nomi",
            example = "Profil",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}