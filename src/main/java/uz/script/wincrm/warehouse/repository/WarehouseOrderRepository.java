package uz.script.wincrm.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.warehouse.WarehouseOrder;

import java.util.List;

@Repository
public interface WarehouseOrderRepository extends JpaRepository<WarehouseOrder, Long> {
    List<WarehouseOrder> findAllByWarehouseId(Long warehouseId);
    List<WarehouseOrder> findAllBySupplierId(Long supplierId);
}
