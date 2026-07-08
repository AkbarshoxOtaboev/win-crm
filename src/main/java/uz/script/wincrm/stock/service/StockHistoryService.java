package uz.script.wincrm.stock.service;

import uz.script.wincrm.stock.response.StockHistoryResponse;

import java.math.BigDecimal;
import java.util.List;

public interface StockHistoryService {

    StockHistoryResponse findById(Long id);

    List<StockHistoryResponse> fetchAll();

    List<StockHistoryResponse> fetchByWarehouseId(Long warehouseId);

    List<StockHistoryResponse> fetchByGoodsId(Long goodsId);

    List<StockHistoryResponse> fetchByGoodsIdAndWarehouseId(Long goodsId, Long warehouseId);

    /**
     * Omborga mahsulot kirim qilinganda StockServiceImpl.increaseStock() ichidan chaqiriladi
     * va tarixga IN yozuvi qo'shadi.
     */
    void recordIn(Long goodsId, Long warehouseId, BigDecimal count, BigDecimal balanceAfter, String comment);

    /**
     * Ombordan mahsulot chiqim qilinganda StockServiceImpl.decreaseStock() ichidan chaqiriladi
     * va tarixga OUT yozuvi qo'shadi.
     */
    void recordOut(Long goodsId, Long warehouseId, BigDecimal count, BigDecimal balanceAfter, String comment);
}