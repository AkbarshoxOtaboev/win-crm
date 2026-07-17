package uz.script.wincrm.clients.service;

import uz.script.wincrm.clients.dto.ClientBalanceDTO;
import uz.script.wincrm.clients.response.ClientBalanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface ClientBalanceService {

    /**
     * @param fromDate davr boshlanishi (null bo'lsa - joriy oyning 1-kuni)
     * @param toDate   davr oxiri (null bo'lsa - joriy oyning oxirgi kuni)
     */
    List<ClientBalanceResponse> fetchAll(LocalDate fromDate, LocalDate toDate);

    /**
     * @param fromDate davr boshlanishi (null bo'lsa - joriy oyning 1-kuni)
     * @param toDate   davr oxiri (null bo'lsa - joriy oyning oxirgi kuni)
     */
    ClientBalanceResponse findByClientId(Long clientId, LocalDate fromDate, LocalDate toDate);

    /**
     * Davr bilan bog'liq bo'lmagan, bazada saqlangan barcha vaqt (all-time) balansini qaytaradi.
     * recalculateClientBalance() chaqirilgandan keyin natijani ko'rsatish uchun ishlatiladi.
     */
    ClientBalanceResponse findAllTimeByClientId(Long clientId);

    /**
     * SaleOrder yoki Payment repository'laridan client bo'yicha summalarni qayta yig'ib,
     * ClientBalance yozuvini (mavjud bo'lmasa - yaratib) yangilaydi.
     * SaleOrderServiceImpl.create() va PaymentServiceImpl.create() ichida chaqiriladi.
     */
    void recalculateClientBalance(Long clientId);

    ClientBalanceResponse adjustBalance(Long clientId, ClientBalanceDTO dto);
}