package uz.script.wincrm.stock.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.stock.Stock;
import uz.script.wincrm.stock.response.StockResponse;

@Component
public class StockMapper {

    public StockResponse toResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .goodsId(stock.getGoods() != null ? stock.getGoods().getId() : null)
                .goodsName(stock.getGoods() != null ? stock.getGoods().getName() : null)
                .warehouseId(stock.getWarehouse() != null ? stock.getWarehouse().getId() : null)
                .warehouseName(stock.getWarehouse() != null ? stock.getWarehouse().getName() : null)
                .count(stock.getCount())
                .status(stock.getStatus())
                .createdAt(stock.getCreatedAt())
                .updatedAt(stock.getUpdatedAt())
                .createdUsername(stock.getCreatedUsername())
                .build();
    }
}