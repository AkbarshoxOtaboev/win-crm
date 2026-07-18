package uz.script.wincrm.inventory.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.inventory.InventoryCheck;
import uz.script.wincrm.inventory.InventoryCheckItem;
import uz.script.wincrm.inventory.response.InventoryCheckItemResponse;
import uz.script.wincrm.inventory.response.InventoryCheckResponse;

import java.util.List;

@Component
public class InventoryCheckMapper {

    public InventoryCheckItemResponse toItemResponse(InventoryCheckItem item) {
        return InventoryCheckItemResponse.builder()
                .id(item.getId())
                .goodsId(item.getGoods() != null ? item.getGoods().getId() : null)
                .goodsName(item.getGoods() != null ? item.getGoods().getName() : null)
                .systemCount(item.getSystemCount())
                .actualCount(item.getActualCount())
                .difference(item.getDifference())
                .comment(item.getComment())
                .build();
    }

    public InventoryCheckResponse toResponse(InventoryCheck check) {
        List<InventoryCheckItemResponse> items = check.getItems() == null
                ? List.of()
                : check.getItems().stream().map(this::toItemResponse).toList();

        return InventoryCheckResponse.builder()
                .id(check.getId())
                .warehouseId(check.getWarehouse() != null ? check.getWarehouse().getId() : null)
                .warehouseName(check.getWarehouse() != null ? check.getWarehouse().getName() : null)
                .checkStatus(check.getCheckStatus())
                .comment(check.getComment())
                .confirmedAt(check.getConfirmedAt())
                .confirmedUsername(check.getConfirmedUsername())
                .items(items)
                .status(check.getStatus())
                .createdAt(check.getCreatedAt())
                .createdUsername(check.getCreatedUsername())
                .build();
    }
}