package uz.script.wincrm.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "Goods DTO", description = "Goods creation and update request")
public class GoodsDTO {

    @NotBlank(message = "Name is required")
    @Schema(
            description = "Goods name",
            example = "Coca-Cola 1.5L",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotNull(message = "Goods group id is required")
    @Schema(
            description = "Goods group identifier",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long goodsGroupId;

    @NotNull(message = "Unit type id is required")
    @Schema(
            description = "Unit type identifier",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long unitTypeId;

    @NotNull(message = "Price cost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cost must not be negative")
    @Schema(
            description = "Cost price",
            example = "8500.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal priceCost;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Selling price must not be negative")
    @Schema(
            description = "Selling price",
            example = "12000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal priceSelling;

    @NotBlank(message = "Barcode is required")
    @Schema(
            description = "Goods barcode",
            example = "4870204012345",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String barcode;

    @Schema(
            description = "Goods photo file. Required on create; optional on update (existing photo is kept if omitted).",
            type = "string",
            format = "binary",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private MultipartFile photo;
}