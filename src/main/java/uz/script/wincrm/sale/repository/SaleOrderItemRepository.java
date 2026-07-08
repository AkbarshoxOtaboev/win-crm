package uz.script.wincrm.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
}