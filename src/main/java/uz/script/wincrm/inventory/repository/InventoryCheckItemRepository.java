package uz.script.wincrm.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.inventory.InventoryCheckItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryCheckItemRepository extends JpaRepository<InventoryCheckItem, Long> {

    List<InventoryCheckItem> findAllByInventoryCheckId(Long inventoryCheckId);

    Optional<InventoryCheckItem> findByIdAndInventoryCheckId(Long id, Long inventoryCheckId);
}