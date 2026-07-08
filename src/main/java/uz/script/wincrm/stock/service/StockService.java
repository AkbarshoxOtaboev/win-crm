package uz.script.wincrm.stock.service;

import uz.script.wincrm.stock.response.StockResponse;

import java.math.BigDecimal;
import java.util.List;

public interface StockService {

    StockResponse findById(Long id);

    List<StockResponse> fetchAll();

    List<StockResponse> fetchByWarehouseId(Long warehouseId);

    List<StockResponse> fetchByGoodsId(Long goodsId);

    /**
     * Omborga mahsulot kirim qilinganda (masalan WarehouseOrderItem yaratilganda) chaqiriladi.
     * Agar shu goods + warehouse bo'yicha Stock allaqachon mavjud bo'lsa - count ustiga qo'shiladi,
     * mavjud bo'lmasa - yangi Stock yozuvi yaratiladi. Bu metoddan tashqari Stock hech qayerda
     * to'g'ridan-to'g'ri yaratilmasligi kerak.
     */
    void increaseStock(Long goodsId, Long warehouseId, BigDecimal count);
    BigDecimal getAvailableStock(Long goodsId, Long warehouseId);
    /**
     * Ombordan mahsulot chiqim qilinganda (sotuv, ombordan-omborga ko'chirish va h.k.) chaqiriladi.
     */
    void decreaseStock(Long goodsId, Long warehouseId, BigDecimal count);

    void delete(Long id);
}