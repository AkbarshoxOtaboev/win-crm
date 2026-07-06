package uz.script.wincrm.goods.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.GoodsGroup;
import uz.script.wincrm.goods.UnitType;
import uz.script.wincrm.goods.dto.GoodsDTO;
import uz.script.wincrm.goods.response.GoodsResponse;
import uz.script.wincrm.utils.Status;

@Component
public class GoodsMapper {

    public Goods toEntity(GoodsDTO dto, GoodsGroup goodsGroup, UnitType unitType, String photoPath) {
        return Goods.builder()
                .name(dto.getName())
                .goodsGroup(goodsGroup)
                .unitType(unitType)
                .priceCost(dto.getPriceCost())
                .priceSelling(dto.getPriceSelling())
                .barcode(dto.getBarcode())
                .photo(photoPath)
                .status(Status.ACTIVE)
                .build();
    }

    public void updateEntity(Goods goods, GoodsDTO dto, GoodsGroup goodsGroup, UnitType unitType, String photoPath) {
        goods.setName(dto.getName());
        goods.setGoodsGroup(goodsGroup);
        goods.setUnitType(unitType);
        goods.setPriceCost(dto.getPriceCost());
        goods.setPriceSelling(dto.getPriceSelling());
        goods.setBarcode(dto.getBarcode());
        if (photoPath != null) {
            goods.setPhoto(photoPath);
        }
    }

    public GoodsResponse toResponse(Goods goods) {
        return GoodsResponse.builder()
                .id(goods.getId())
                .name(goods.getName())
                .goodsGroupId(goods.getGoodsGroup() != null ? goods.getGoodsGroup().getId() : null)
                .goodsGroupName(goods.getGoodsGroup() != null ? goods.getGoodsGroup().getName() : null)
                .unitTypeId(goods.getUnitType() != null ? goods.getUnitType().getId() : null)
                .unitTypeName(goods.getUnitType() != null ? goods.getUnitType().getName() : null)
                .priceCost(goods.getPriceCost())
                .priceSelling(goods.getPriceSelling())
                .barcode(goods.getBarcode())
                .photo(goods.getPhoto())
                .status(goods.getStatus())
                .createdAt(goods.getCreatedAt())
                .updatedAt(goods.getUpdatedAt())
                .createdUsername(goods.getCreatedUsername())
                .build();
    }
}