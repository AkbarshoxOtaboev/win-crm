package uz.script.wincrm.sale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.sale.DebtNotificationHistory;

@Repository
public interface DebtNotificationHistoryRepository extends JpaRepository<DebtNotificationHistory, Long> {

    Page<DebtNotificationHistory> findByClientId(Long clientId, Pageable pageable);
}