package uz.script.wincrm.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.stock.StockHistory;
import uz.script.wincrm.stock.enums.StockStatus;

import java.util.List;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    List<StockHistory> findAllByWarehouseId(Long warehouseId);
    List<StockHistory> findAllByGoodsId(Long goodsId);
    List<StockHistory> findAllByGoodsIdAndWarehouseId(Long goodsId, Long warehouseId);
    List<StockHistory> findAllByStockStatus(StockStatus stockStatus);
}