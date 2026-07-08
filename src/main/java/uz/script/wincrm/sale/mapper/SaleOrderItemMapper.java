package uz.script.wincrm.sale.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.sale.SaleOrderItem;
import uz.script.wincrm.sale.dto.SaleOrderItemDTO;
import uz.script.wincrm.sale.response.SaleOrderItemResponse;

@Component
public class SaleOrderItemMapper {

    public SaleOrderItem toEntity(SaleOrderItemDTO dto) {
        if (dto == null) {
            return null;
        }
        return SaleOrderItem.builder()
                .priceCost(dto.getPriceCost())
                .priceSelling(dto.getPriceSelling())
                .width(dto.getWidth())
                .height(dto.getHeight())
                .count(dto.getCount())
                .arrivalDate(dto.getArrivalDate())
                .build();
    }

    public void updateEntity(SaleOrderItem entity, SaleOrderItemDTO dto) {
        if (dto.getPriceCost() != null) {
            entity.setPriceCost(dto.getPriceCost());
        }
        if (dto.getPriceSelling() != null) {
            entity.setPriceSelling(dto.getPriceSelling());
        }
        if (dto.getWidth() != null) {
            entity.setWidth(dto.getWidth());
        }
        if (dto.getHeight() != null) {
            entity.setHeight(dto.getHeight());
        }
        if (dto.getCount() != null) {
            entity.setCount(dto.getCount());
        }
        if (dto.getArrivalDate() != null) {
            entity.setArrivalDate(dto.getArrivalDate());
        }
    }

    public SaleOrderItemResponse toResponse(SaleOrderItem entity) {
        if (entity == null) {
            return null;
        }
        return SaleOrderItemResponse.builder()
                .id(entity.getId())
                .warehouseId(entity.getWarehouse() != null ? entity.getWarehouse().getId() : null)
                .warehouseName(entity.getWarehouse() != null ? entity.getWarehouse().getName() : null)
                .saleOrderId(entity.getSaleOrder() != null ? entity.getSaleOrder().getId() : null)
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .clientFullName(entity.getClient() != null ? entity.getClient().getFullName() : null)
                .goodsId(entity.getGoods() != null ? entity.getGoods().getId() : null)
                .goodsName(entity.getGoods() != null ? entity.getGoods().getName() : null)
                .priceCost(entity.getPriceCost())
                .priceSelling(entity.getPriceSelling())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .count(entity.getCount())
                .arrivalDate(entity.getArrivalDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdUsername(entity.getCreatedUsername())
                .build();
    }
}