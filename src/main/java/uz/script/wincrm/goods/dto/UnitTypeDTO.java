package uz.script.wincrm.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Unit Type yaratish va tahrirlash uchun DTO")
public class UnitTypeDTO {

    @NotBlank(message = "Nomi bo'sh bo'lishi mumkin emas")
    @Size(max = 100, message = "Nomi 100 ta belgidan oshmasligi kerak")
    @Schema(
            description = "O'lchov birligi nomi",
            example = "Dona",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}