package uz.script.wincrm.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.sale.SaleOrderHistory;

import java.util.List;

@Repository
public interface SaleOrderHistoryRepository extends JpaRepository<SaleOrderHistory, Long> {

    /**
     * Bitta buyurtmaning to'liq xronologiyasini eng eskisidan eng yangisiga
     * qarab (createdAt ASC) qaytaradi - timeline ko'rinishida chizish uchun qulay.
     */
    List<SaleOrderHistory> findBySaleOrder_IdOrderByCreatedAtAsc(Long saleOrderId);
}