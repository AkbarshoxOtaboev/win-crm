package uz.script.wincrm.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.sale.repository.SaleOrderItemRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private static final int TOP_LIMIT = 10;

    private final SaleOrderItemRepository repository;

    @Override
    public List<TopGoodsResponse> fetchTopGoodsByQuantity(LocalDate startDate, LocalDate endDate) {
        log.info("Fetch TOP {} goods by quantity: {} - {}", TOP_LIMIT, startDate, endDate);

        LocalDateTime[] range = toDateTimeRange(startDate, endDate);
        Pageable top = PageRequest.of(0, TOP_LIMIT);

        return repository.findTopGoodsByQuantity(range[0], range[1], top);
    }

    @Override
    public List<TopGoodsResponse> fetchTopGoodsByAmount(LocalDate startDate, LocalDate endDate) {
        log.info("Fetch TOP {} goods by amount: {} - {}", TOP_LIMIT, startDate, endDate);

        LocalDateTime[] range = toDateTimeRange(startDate, endDate);
        Pageable top = PageRequest.of(0, TOP_LIMIT);

        return repository.findTopGoodsByAmount(range[0], range[1], top);
    }

    @Override
    public List<GoodsGroupSummaryResponse> fetchGoodsGroupSummary(LocalDate startDate, LocalDate endDate) {
        log.info("Fetch goods group summary: {} - {}", startDate, endDate);

        LocalDateTime[] range = toDateTimeRange(startDate, endDate);

        return repository.findGoodsGroupSummary(range[0], range[1]);
    }

    /**
     * LocalDate oralig'ini to'liq LocalDateTime oralig'iga o'giradi:
     * startDate 00:00:00'dan, endDate 23:59:59.999999999'gacha.
     */
    private LocalDateTime[] toDateTimeRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BadRequestException("startDate va endDate majburiy");
        }
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("startDate endDate'dan katta bo'lishi mumkin emas");
        }

        return new LocalDateTime[]{startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)};
    }
}