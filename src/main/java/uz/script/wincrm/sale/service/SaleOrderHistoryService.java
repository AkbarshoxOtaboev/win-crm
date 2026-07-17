package uz.script.wincrm.sale.service;

import uz.script.wincrm.sale.dto.SaleOrderHistoryDTO;
import uz.script.wincrm.sale.response.SaleOrderHistoryResponse;

import java.util.List;

public interface SaleOrderHistoryService {

    /**
     * Berilgan sale order'ga tegishli barcha status o'zgarishlarini
     * xronologik tartibda (eskisidan yangisiga) qaytaradi.
     */
    List<SaleOrderHistoryResponse> fetchBySaleOrderId(Long saleOrderId);

    /**
     * Yangi tarix yozuvini saqlaydi. Faqat SaleOrderServiceImpl ichidan
     * (create() va changeStatus() metodlarida) chaqirilishi kerak - tashqi
     * controller orqali to'g'ridan-to'g'ri ochilmaydi.
     */
    void recordHistory(SaleOrderHistoryDTO dto);
}