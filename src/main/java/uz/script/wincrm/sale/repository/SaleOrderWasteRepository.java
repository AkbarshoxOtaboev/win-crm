package uz.script.wincrm.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.sale.SaleOrderWaste;
import uz.script.wincrm.sale.response.SaleOrderWasteSummaryResponse;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleOrderWasteRepository extends JpaRepository<SaleOrderWaste, Long> {

    List<SaleOrderWaste> findAllBySaleOrderId(Long saleOrderId);

    Page<SaleOrderWaste> findAllBySaleOrderId(Long saleOrderId, Pageable pageable);

    Page<SaleOrderWaste> findByGoodsId(Long goodsId, Pageable pageable);

    /**
     * Berilgan buyurtma bo'yicha jami ortib qolgan miqdorni (barcha materiallar yig'indisi) qaytaradi.
     */
    @Query("SELECT COALESCE(SUM(w.quantity), 0) FROM SaleOrderWaste w WHERE w.saleOrder.id = :saleOrderId")
    BigDecimal sumQuantityBySaleOrderId(@Param("saleOrderId") Long saleOrderId);

    /**
     * Berilgan material (Goods) bo'yicha barcha buyurtmalardagi jami ortib qolgan miqdorni qaytaradi.
     */
    @Query("SELECT COALESCE(SUM(w.quantity), 0) FROM SaleOrderWaste w WHERE w.goods.id = :goodsId")
    BigDecimal sumQuantityByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * Berilgan buyurtma bo'yicha material (Goods) kesimida jamlangan ortib qolgan miqdorlar.
     */
    @Query("SELECT new uz.script.wincrm.sale.response.SaleOrderWasteSummaryResponse(" +
            "w.goods.id, w.goodsName, SUM(w.quantity)) " +
            "FROM SaleOrderWaste w " +
            "WHERE w.saleOrder.id = :saleOrderId " +
            "GROUP BY w.goods.id, w.goodsName " +
            "ORDER BY SUM(w.quantity) DESC")
    List<SaleOrderWasteSummaryResponse> findSummaryBySaleOrderId(@Param("saleOrderId") Long saleOrderId);

    /**
     * Barcha buyurtmalar bo'yicha (butun tizim kesimida) material turi bo'yicha jamlangan
     * ortib qolgan miqdor - qayta ishlatish uchun yetarli qoldiq to'planganini aniqlashda foydali.
     */
    @Query("SELECT new uz.script.wincrm.sale.response.SaleOrderWasteSummaryResponse(" +
            "w.goods.id, w.goodsName, SUM(w.quantity)) " +
            "FROM SaleOrderWaste w " +
            "GROUP BY w.goods.id, w.goodsName " +
            "ORDER BY SUM(w.quantity) DESC")
    List<SaleOrderWasteSummaryResponse> findSummaryGroupedByGoods();
}