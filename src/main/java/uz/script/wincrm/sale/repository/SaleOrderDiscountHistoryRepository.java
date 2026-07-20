package uz.script.wincrm.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.sale.SaleOrderDiscountHistory;

import java.util.List;

@Repository
public interface SaleOrderDiscountHistoryRepository extends JpaRepository<SaleOrderDiscountHistory, Long> {

    /**
     * Buyurtmaning barcha chegirma o'zgarishlarini eskisidan yangisiga xronologik qaytaradi.
     */
    List<SaleOrderDiscountHistory> findBySaleOrderIdOrderByCreatedAtAsc(Long saleOrderId);
}