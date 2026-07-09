package uz.script.wincrm.goods.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.goods.enums.Type;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Goods Response", description = "Goods information returned by the API")
public class GoodsResponse {

    @Schema(
            description = "Unique goods identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Goods name",
            example = "Coca-Cola 1.5L"
    )
    private String name;

    @Schema(
            description = "Goods group identifier",
            example = "1"
    )
    private Long goodsGroupId;

    @Schema(
            description = "Goods group name",
            example = "Beverages"
    )
    private String goodsGroupName;

    @Schema(
            description = "Unit type identifier",
            example = "1"
    )
    private Long unitTypeId;

    @Schema(
            description = "Unit type name",
            example = "Piece"
    )
    private String unitTypeName;

    @Schema(
            description = "Goods type",
            example = "PRODUCT",
            implementation = Type.class
    )
    private Type type;

    @Schema(
            description = "Goods type human-readable label",
            example = "Tovar"
    )
    private String typeLabel;

    @Schema(
            description = "Cost price",
            example = "8500.00"
    )
    private BigDecimal priceCost;

    @Schema(
            description = "Selling price",
            example = "12000.00"
    )
    private BigDecimal priceSelling;

    @Schema(
            description = "Goods barcode",
            example = "4870204012345"
    )
    private String barcode;

    @Schema(
            description = "Goods photo URL or path",
            example = "https://cdn.wincrm.uz/goods/12345.png"
    )
    private String photo;

    @Schema(
            description = "Current goods status",
            example = "ACTIVE",
            implementation = Status.class
    )
    private Status status;

    @Schema(
            description = "Date and time when the goods was created",
            example = "2026-07-04T09:30:15"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the goods was last updated",
            example = "2026-07-04T11:45:30"
    )
    private LocalDateTime updatedAt;

    @Schema(
            description = "ID of the user who created the goods",
            example = "1"
    )
    private Long createdBy;
    @Schema(
            description = "Username of the user who created the goods",
            example = "admin"
    )
    private String createdUsername;
}