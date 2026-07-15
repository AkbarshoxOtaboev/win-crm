package uz.script.wincrm.dashboard;

import uz.script.wincrm.dashboard.responses.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {

    /**
     * Berilgan sana oralig'ida eng ko'p miqdorda sotilgan TOP 10 mahsulot.
     */
    List<TopGoodsResponse> fetchTopGoodsByQuantity(LocalDate startDate, LocalDate endDate);

    /**
     * Berilgan sana oralig'ida eng ko'p summada sotilgan TOP 10 mahsulot.
     */
    List<TopGoodsResponse> fetchTopGoodsByAmount(LocalDate startDate, LocalDate endDate);

    /**
     * Berilgan sana oralig'ida GoodsGroup (mahsulot guruhi) bo'yicha jamlangan statistika.
     */
    List<GoodsGroupSummaryResponse> fetchGoodsGroupSummary(LocalDate startDate, LocalDate endDate);

    /**
     * Berilgan sana oralig'ida eng ko'p savdo qilgan TOP 10 sotuvchi (User),
     * umumiy savdo summasi bo'yicha kamayish tartibida.
     */
    List<TopSellerResponse> fetchTopSellers(LocalDate startDate, LocalDate endDate);

    /**
     * Berilgan sana-vaqt oralig'ida (fromDate - toDate) barcha to'lovlarni PaymentType
     * bo'yicha jamlab qaytaradi (butun davr uchun bitta yig'indi har bir tur bo'yicha).
     */
    List<PaymentTypeSummaryResponse> fetchPaymentSummaryByType(LocalDateTime fromDate, LocalDateTime toDate);

    /**
     * Berilgan sana-vaqt oralig'idagi har bir kun uchun (to'lov bo'lmagan kunlar ham
     * 0 summa bilan) jami to'lov summasi va PaymentType bo'yicha taqsimotini qaytaradi.
     */
    List<DailyPaymentSummaryResponse> fetchDailyPayments(LocalDateTime fromDate, LocalDateTime toDate);
}