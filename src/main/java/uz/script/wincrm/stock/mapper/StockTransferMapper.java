package uz.script.wincrm.stock.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.stock.StockTransfer;
import uz.script.wincrm.stock.response.StockTransferResponse;

@Component
public class StockTransferMapper {

    public StockTransferResponse toResponse(StockTransfer transfer) {
        return StockTransferResponse.builder()
                .id(transfer.getId())
                .goodsId(transfer.getGoods() != null ? transfer.getGoods().getId() : null)
                .goodsName(transfer.getGoods() != null ? transfer.getGoods().getName() : null)
                .fromWarehouseId(transfer.getFromWarehouse() != null ? transfer.getFromWarehouse().getId() : null)
                .fromWarehouseName(transfer.getFromWarehouse() != null ? transfer.getFromWarehouse().getName() : null)
                .toWarehouseId(transfer.getToWarehouse() != null ? transfer.getToWarehouse().getId() : null)
                .toWarehouseName(transfer.getToWarehouse() != null ? transfer.getToWarehouse().getName() : null)
                .count(transfer.getCount())
                .comment(transfer.getComment())
                .status(transfer.getStatus())
                .createdAt(transfer.getCreatedAt())
                .updatedAt(transfer.getUpdatedAt())
                .createdUsername(transfer.getCreatedUsername())
                .build();
    }
}
