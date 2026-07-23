package uz.script.wincrm.sale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.sale.dto.SaleOrderWasteDTO;
import uz.script.wincrm.sale.response.SaleOrderWasteResponse;
import uz.script.wincrm.sale.response.SaleOrderWasteSummaryResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SaleOrderWasteService {

    /**
     * Buyurtma bo'yicha ishlab chiqarishdan ortib qolgan materialni qayd etadi.
     * DIQQAT: bu yozuv hech qanday Stock/totalSum/debtSum'ga TA'SIR QILMAYDI -
     * faqat ma'lumot (info) sifatida saqlanadi.
     */
    SaleOrderWasteResponse create(SaleOrderWasteDTO dto);

    SaleOrderWasteResponse findById(Long id);

    Page<SaleOrderWasteResponse> fetchAll(Pageable pageable);

    List<SaleOrderWasteResponse> fetchBySaleOrderId(Long saleOrderId);

    Page<SaleOrderWasteResponse> fetchBySaleOrderIdPaginated(Long saleOrderId, Pageable pageable);

    Page<SaleOrderWasteResponse> fetchByGoodsId(Long goodsId, Pageable pageable);

    SaleOrderWasteResponse update(Long id, SaleOrderWasteDTO dto);

    void delete(Long id);

    /**
     * Berilgan buyurtma bo'yicha jami ortib qolgan miqdorni (barcha materiallar yig'indisi) hisoblab qaytaradi.
     */
    BigDecimal calculateTotalBySaleOrderId(Long saleOrderId);

    /**
     * Berilgan buyurtma bo'yicha material (Goods) kesimida jamlangan hisobotni qaytaradi.
     */
    List<SaleOrderWasteSummaryResponse> fetchSummaryBySaleOrderId(Long saleOrderId);

    /**
     * Barcha buyurtmalar bo'yicha material turi kesimida jamlangan umumiy hisobotni qaytaradi.
     */
    List<SaleOrderWasteSummaryResponse> fetchSummaryGroupedByGoods();
}