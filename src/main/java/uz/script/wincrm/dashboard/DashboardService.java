package uz.script.wincrm.dashboard;

import java.time.LocalDate;
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
}
