package uz.script.wincrm.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.dashboard.responses.GoodsGroupSummaryResponse;
import uz.script.wincrm.dashboard.responses.TopGoodsResponse;
import uz.script.wincrm.goods.enums.Type;
import uz.script.wincrm.sale.SaleOrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleOrderItemRepository extends JpaRepository<SaleOrderItem, Long> {

    List<SaleOrderItem> findAllBySaleOrderId(Long saleOrderId);

    Page<SaleOrderItem> findAllBySaleOrderId(Long saleOrderId, Pageable pageable);

    Page<SaleOrderItem> findByClientId(Long clientId, Pageable pageable);

    Page<SaleOrderItem> findByUserId(Long userId, Pageable pageable);

    Page<SaleOrderItem> findByWarehouseId(Long warehouseId, Pageable pageable);

    Page<SaleOrderItem> findByGoodsId(Long goodsId, Pageable pageable);

    List<SaleOrderItem> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Ombarning konkret mahsuloti uchun jami stockni hisoblaydi
     */
    @Query("SELECT COALESCE(SUM(soi.count), 0) FROM SaleOrderItem soi " +
            "WHERE soi.warehouse.id = :warehouseId AND soi.goods.id = :goodsId AND soi.status <> 'DELETED'")
    BigDecimal getTotalStockByWarehouseAndGoods(
            @Param("warehouseId") Long warehouseId,
            @Param("goodsId") Long goodsId
    );

    @Query("SELECT soi FROM SaleOrderItem soi WHERE soi.saleOrder.id = :saleOrderId AND soi.goods.id = :goodsId")
    Optional<SaleOrderItem> findBySaleOrderIdAndGoodsId(
            @Param("saleOrderId") Long saleOrderId,
            @Param("goodsId") Long goodsId
    );

    /**
     * Goods.type va berilgan sana oralig'i (kunlik/haftalik/oylik) bo'yicha filtrlaydi.
     */
    @Query("SELECT soi FROM SaleOrderItem soi " +
            "WHERE soi.goods.type = :type AND soi.arrivalDate BETWEEN :startDate AND :endDate")
    Page<SaleOrderItem> findByGoodsTypeAndArrivalDateBetween(
            @Param("type") Type type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    // ==== DASHBOARD STATISTIKASI ====

    /**
     * Berilgan sana oralig'ida eng ko'p miqdorda sotilgan mahsulotlar (kamayish tartibida).
     * TOP N olish uchun Pageable ishlatiladi, masalan PageRequest.of(0, 10).
     */
    @Query("SELECT new uz.script.wincrm.dashboard.responses.TopGoodsResponse(" +
            "soi.goods.id, soi.goods.name, SUM(soi.count), SUM(soi.count * soi.priceSelling)) " +
            "FROM SaleOrderItem soi " +
            "WHERE soi.arrivalDate BETWEEN :startDate AND :endDate " +
            "GROUP BY soi.goods.id, soi.goods.name " +
            "ORDER BY SUM(soi.count) DESC")
    List<TopGoodsResponse> findTopGoodsByQuantity(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Berilgan sana oralig'ida eng ko'p summada sotilgan mahsulotlar (kamayish tartibida).
     */
    @Query("SELECT new uz.script.wincrm.dashboard.responses.TopGoodsResponse(" +
            "soi.goods.id, soi.goods.name, SUM(soi.count), SUM(soi.count * soi.priceSelling)) " +
            "FROM SaleOrderItem soi " +
            "WHERE soi.arrivalDate BETWEEN :startDate AND :endDate " +
            "GROUP BY soi.goods.id, soi.goods.name " +
            "ORDER BY SUM(soi.count * soi.priceSelling) DESC")
    List<TopGoodsResponse> findTopGoodsByAmount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Berilgan sana oralig'ida GoodsGroup bo'yicha jamlangan miqdor va summa (kamayish tartibida).
     */
    @Query("SELECT new uz.script.wincrm.dashboard.responses.GoodsGroupSummaryResponse(" +
            "soi.goods.goodsGroup.id, soi.goods.goodsGroup.name, SUM(soi.count), SUM(soi.count * soi.priceSelling)) " +
            "FROM SaleOrderItem soi " +
            "WHERE soi.arrivalDate BETWEEN :startDate AND :endDate " +
            "GROUP BY soi.goods.goodsGroup.id, soi.goods.goodsGroup.name " +
            "ORDER BY SUM(soi.count * soi.priceSelling) DESC")
    List<GoodsGroupSummaryResponse> findGoodsGroupSummary(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}