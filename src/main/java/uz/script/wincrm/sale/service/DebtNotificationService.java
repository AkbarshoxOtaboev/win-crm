package uz.script.wincrm.sale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.sale.response.DebtNotificationHistoryResponse;
import uz.script.wincrm.sale.response.DebtorClientResponse;

import java.time.LocalDate;
import java.util.List;

public interface DebtNotificationService {

    /**
     * Qarzi bor mijozlar ro'yxatini ixtiyoriy filtrlar bilan qaytaradi — admin panelda
     * ko'rsatish va SMS yuborish uchun tanlash imkonini beradi.
     *
     * @param startDate sana oralig'i boshi (ixtiyoriy, null bo'lsa cheklanmaydi)
     * @param endDate   sana oralig'i oxiri (ixtiyoriy, null bo'lsa cheklanmaydi)
     * @param userId    faqat shu xodimga (User) biriktirilgan buyurtmalar (ixtiyoriy)
     */
    List<DebtorClientResponse> fetchDebtorClients(LocalDate startDate, LocalDate endDate, Long userId);

    /**
     * Admin tanlagan bitta mijozga, uning barcha qarzlari yig'indisi
     * haqida SMS yuboradi.
     */
    void sendDebtNotificationToClient(Long clientId);

    /**
     * Admin panelda tanlangan bir nechta mijozga SMS yuboradi.
     */
    void sendDebtNotificationsToClients(List<Long> clientIds);

    /**
     * Bitta konkret SaleOrder bo'yicha, unga biriktirilgan mijozga
     * qarz haqida SMS yuboradi.
     */
    void sendDebtNotificationForOrder(Long saleOrderId);

    /**
     * Yuborilgan barcha SMS'lar tarixi (muvaffaqiyatli va muvaffaqiyatsiz).
     */
    Page<DebtNotificationHistoryResponse> fetchHistory(Pageable pageable);

    /**
     * Bitta mijozga yuborilgan SMS'lar tarixi.
     */
    Page<DebtNotificationHistoryResponse> fetchHistoryByClientId(Long clientId, Pageable pageable);
}