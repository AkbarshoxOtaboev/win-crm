package uz.script.wincrm.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.inventory.InventoryCheck;
import uz.script.wincrm.inventory.enums.InventoryCheckStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryCheckRepository extends JpaRepository<InventoryCheck, Long> {

    List<InventoryCheck> findAllByWarehouseId(Long warehouseId);

    Optional<InventoryCheck> findByWarehouseIdAndCheckStatus(Long warehouseId, InventoryCheckStatus checkStatus);
}