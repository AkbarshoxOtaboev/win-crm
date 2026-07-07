package uz.script.wincrm.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.warehouse.WarehouseOrderItem;

import java.util.List;

@Repository
public interface WarehouseOrderItemRepository extends JpaRepository<WarehouseOrderItem, Long> {
    List<WarehouseOrderItem> findAllByWarehouseOrderId(Long warehouseOrderId);
    List<WarehouseOrderItem> findAllByWarehouseId(Long warehouseId);
}
