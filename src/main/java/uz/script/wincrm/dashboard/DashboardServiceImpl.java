package uz.script.wincrm.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.dashboard.responses.*;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.payment.Payment;
import uz.script.wincrm.payment.PaymentType;
import uz.script.wincrm.payment.repository.PaymentRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.sale.repository.SaleOrderItemRepository;
import uz.script.wincrm.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private static final int TOP_LIMIT = 10;

    private final SaleOrderItemRepository repository;
    private final SaleOrderRepository saleOrderRepository;
    private final PaymentRepository paymentRepository;

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

    @Override
    public List<TopSellerResponse> fetchTopSellers(LocalDate startDate, LocalDate endDate) {
        log.info("Fetch TOP {} sellers: {} - {}", TOP_LIMIT, startDate, endDate);

        LocalDateTime[] range = toDateTimeRange(startDate, endDate);
        Pageable top = PageRequest.of(0, TOP_LIMIT);

        List<Object[]> rows = saleOrderRepository.findTopSellersByAmount(range[0], range[1], top);

        return rows.stream()
                .map(row -> {
                    User user = (User) row[0];
                    Long orderCount = (Long) row[1];
                    BigDecimal totalAmount = (BigDecimal) row[2];

                    return new TopSellerResponse(
                            user.getId(),
                            resolveUserName(user),
                            orderCount,
                            totalAmount
                    );
                })
                .toList();
    }

    @Override
    public List<PaymentTypeSummaryResponse> fetchPaymentSummaryByType(LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetch payment summary by type: {} - {}", fromDate, toDate);

        validateRange(fromDate, toDate);

        List<Payment> payments = paymentRepository.findAllInRangeWithType(fromDate, toDate);

        Map<PaymentType, List<Payment>> byType = payments.stream()
                .filter(p -> p.getPaymentType() != null)
                .collect(Collectors.groupingBy(Payment::getPaymentType));

        return byType.entrySet().stream()
                .map(entry -> PaymentTypeSummaryResponse.builder()
                        .paymentTypeId(entry.getKey().getId())
                        .paymentTypeName(entry.getKey().getName())
                        .totalAmount(entry.getValue().stream()
                                .map(Payment::getPaymentAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .paymentCount(entry.getValue().size())
                        .build())
                .sorted(Comparator.comparing(PaymentTypeSummaryResponse::getTotalAmount).reversed())
                .toList();
    }

    @Override
    public List<DailyPaymentSummaryResponse> fetchDailyPayments(LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetch daily payments: {} - {}", fromDate, toDate);

        validateRange(fromDate, toDate);

        List<Payment> payments = paymentRepository.findAllInRangeWithType(fromDate, toDate);

        Map<LocalDate, List<Payment>> byDate = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentDate().toLocalDate()));

        List<DailyPaymentSummaryResponse> result = new ArrayList<>();

        LocalDate cursor = fromDate.toLocalDate();
        LocalDate lastDay = toDate.toLocalDate();

        // Har bir kun uchun (to'lov bo'lmagan kunlar ham 0 summa bilan) qator hosil qilamiz
        while (!cursor.isAfter(lastDay)) {
            List<Payment> dayPayments = byDate.getOrDefault(cursor, List.of());

            Map<PaymentType, BigDecimal> amountByType = dayPayments.stream()
                    .filter(p -> p.getPaymentType() != null)
                    .collect(Collectors.groupingBy(
                            Payment::getPaymentType,
                            Collectors.reducing(BigDecimal.ZERO, Payment::getPaymentAmount, BigDecimal::add)
                    ));

            List<DailyPaymentSummaryResponse.PaymentTypeAmount> byType = amountByType.entrySet().stream()
                    .map(entry -> DailyPaymentSummaryResponse.PaymentTypeAmount.builder()
                            .paymentTypeId(entry.getKey().getId())
                            .paymentTypeName(entry.getKey().getName())
                            .amount(entry.getValue())
                            .build())
                    .toList();

            BigDecimal dayTotal = dayPayments.stream()
                    .map(Payment::getPaymentAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.add(DailyPaymentSummaryResponse.builder()
                    .date(cursor)
                    .totalAmount(dayTotal)
                    .byType(byType)
                    .build());

            cursor = cursor.plusDays(1);
        }

        return result;
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

    private void validateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null || toDate == null) {
            throw new BadRequestException("fromDate va toDate majburiy");
        }
        if (fromDate.isAfter(toDate)) {
            throw new BadRequestException("fromDate toDate'dan katta bo'lishi mumkin emas");
        }
    }

    /**
     * TODO: User entity'sidagi haqiqiy ism maydoniga moslang.
     * Hozircha getFullName() deb taxmin qilindi — agar firstName/lastName
     * alohida bo'lsa, shu yerni o'zgartiring, masalan:
     * user.getFirstName() + " " + user.getLastName()
     */
    private String resolveUserName(User user) {
        return user.getFullName();
    }
}