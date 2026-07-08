package uz.script.wincrm.stock.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.stock.StockHistory;
import uz.script.wincrm.stock.response.StockHistoryResponse;

@Component
public class StockHistoryMapper {

    public StockHistoryResponse toResponse(StockHistory history) {
        return StockHistoryResponse.builder()
                .id(history.getId())
                .goodsId(history.getGoods() != null ? history.getGoods().getId() : null)
                .goodsName(history.getGoods() != null ? history.getGoods().getName() : null)
                .warehouseId(history.getWarehouse() != null ? history.getWarehouse().getId() : null)
                .warehouseName(history.getWarehouse() != null ? history.getWarehouse().getName() : null)
                .stockStatus(history.getStockStatus())
                .count(history.getCount())
                .balanceAfter(history.getBalanceAfter())
                .comment(history.getComment())
                .createdAt(history.getCreatedAt())
                .updatedAt(history.getUpdatedAt())
                .createdUsername(history.getCreatedUsername())
                .build();
    }
}