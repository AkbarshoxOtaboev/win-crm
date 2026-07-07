package uz.script.wincrm.warehouse.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.WarehouseOrder;
import uz.script.wincrm.warehouse.WarehouseOrderItem;
import uz.script.wincrm.warehouse.dto.WarehouseOrderItemDTO;
import uz.script.wincrm.warehouse.response.WarehouseOrderItemResponse;

@Component
public class WarehouseOrderItemMapper {

    public WarehouseOrderItem toEntity(
            WarehouseOrderItemDTO dto,
            Warehouse warehouse,
            WarehouseOrder warehouseOrder,
            Supplier supplier,
            Goods goods
    ) {
        return WarehouseOrderItem.builder()
                .warehouse(warehouse)
                .warehouseOrder(warehouseOrder)
                .supplier(supplier)
                .goods(goods)
                .priceCost(dto.getPriceCost())
                .priceSelling(dto.getPriceSelling())
                .weight(dto.getWeight())
                .height(dto.getHeight())
                .count(dto.getCount())
                .arrivalDate(dto.getArrivalDate())
                .status(Status.ACTIVE)
                .build();
    }

    public void updateEntity(
            WarehouseOrderItem item,
            WarehouseOrderItemDTO dto,
            Warehouse warehouse,
            WarehouseOrder warehouseOrder,
            Supplier supplier,
            Goods goods
    ) {
        item.setWarehouse(warehouse);
        item.setWarehouseOrder(warehouseOrder);
        item.setSupplier(supplier);
        item.setGoods(goods);
        item.setPriceCost(dto.getPriceCost());
        item.setPriceSelling(dto.getPriceSelling());
        item.setWeight(dto.getWeight());
        item.setHeight(dto.getHeight());
        item.setCount(dto.getCount());
        item.setArrivalDate(dto.getArrivalDate());
    }

    public WarehouseOrderItemResponse toResponse(WarehouseOrderItem item) {
        return WarehouseOrderItemResponse.builder()
                .id(item.getId())
                .warehouseId(item.getWarehouse() != null ? item.getWarehouse().getId() : null)
                .warehouseName(item.getWarehouse() != null ? item.getWarehouse().getName() : null)
                .warehouseOrderId(item.getWarehouseOrder() != null ? item.getWarehouseOrder().getId() : null)
                .supplierId(item.getSupplier() != null ? item.getSupplier().getId() : null)
                .supplierName(item.getSupplier() != null ? item.getSupplier().getName() : null)
                .goodsId(item.getGoods() != null ? item.getGoods().getId() : null)
                .goodsName(item.getGoods() != null ? item.getGoods().getName() : null)
                .priceCost(item.getPriceCost())
                .priceSelling(item.getPriceSelling())
                .weight(item.getWeight())
                .height(item.getHeight())
                .count(item.getCount())
                .arrivalDate(item.getArrivalDate())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .createdUsername(item.getCreatedUsername())
                .build();
    }
}
