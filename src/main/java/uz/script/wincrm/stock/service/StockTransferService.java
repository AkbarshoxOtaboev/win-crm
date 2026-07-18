package uz.script.wincrm.stock.service;

import uz.script.wincrm.stock.request.StockTransferRequest;
import uz.script.wincrm.stock.response.StockTransferResponse;

import java.util.List;

public interface StockTransferService {

    /**
     * Bitta ombordan (fromWarehouseId) ikkinchi omborga (toWarehouseId) mahsulot ko'chiradi.
     * Ichida mavjud StockService.decreaseStock (manba ombor) va StockService.increaseStock
     * (maqsad ombor) metodlari chaqiriladi — shu bilan birga StockHistory yozuvlari
     * (OUT va IN) avtomatik yaratiladi. Yakunida audit/hisobot maqsadida StockTransfer
     * yozuvi saqlanadi. Butun operatsiya bitta tranzaksiyada bajariladi.
     */
    StockTransferResponse transfer(StockTransferRequest request);

    StockTransferResponse findById(Long id);

    List<StockTransferResponse> fetchAll();

    /**
     * Berilgan ombor manba yoki maqsad sifatida ishtirok etgan barcha transferlarni qaytaradi.
     */
    List<StockTransferResponse> fetchByWarehouseId(Long warehouseId);

    List<StockTransferResponse> fetchByGoodsId(Long goodsId);

    /**
     * Faqat transfer tarixi yozuvini soft-delete qiladi (status = DELETED);
     * bu metod ombordagi count'larni avtomatik qaytarmaydi.
     */
    void delete(Long id);
}
