package uz.script.wincrm.telegram.service;

import uz.script.wincrm.telegram.view.ClientBalanceView;
import uz.script.wincrm.telegram.view.PaymentView;
import uz.script.wincrm.telegram.view.SaleOrderView;

import java.util.List;
import java.util.Optional;

/**
 * Telegram bot uchun MAXSUS o'qish-uchun (read-only) ma'lumot qatlami.
 * <p>
 * MUHIM: CrmTelegramBot Spring bean emas va Telegram Long Polling thread'ida
 * ishlaydi (odatiy request-scoped transaction yo'q). Shu sababli u hech qachon
 * Client/SaleOrder/Payment entity'larining lazy collection yoki lazy
 * relation'larini bevosita navigatsiya qilmasligi kerak (LazyInitializationException
 * xavfi). Barcha bot ma'lumotlari shu servis orqali, @Transactional(readOnly = true)
 * chegarasi ichida, JPQL constructor-expression proyeksiyalar (View recordlar)
 * sifatida olinadi.
 */
public interface TelegramBotDataService {

    List<SaleOrderView> findOrdersByClientId(Long clientId);

    Optional<SaleOrderView> findOrderByIdAndClientId(Long orderId, Long clientId);

    List<PaymentView> findPaymentsByClientId(Long clientId);

    List<PaymentView> findPaymentsByOrderId(Long orderId);

    Optional<ClientBalanceView> findBalanceByClientId(Long clientId);
}