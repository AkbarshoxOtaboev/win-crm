package uz.script.wincrm.sale.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.SaleOrderDiscountHistory;
import uz.script.wincrm.sale.dto.SaleOrderDiscountHistoryDTO;
import uz.script.wincrm.sale.repository.SaleOrderDiscountHistoryRepository;
import uz.script.wincrm.sale.repository.SaleOrderRepository;

/**
 * Chegirma tarixini ALOHIDA tranzaksiyada (REQUIRES_NEW) yozadi.
 * DebtNotificationHistoryRecorder bilan bir xil pattern: audit yozuvi mantiqan mustaqil,
 * shu tufayli tashqi tranzaksiyaga bog'liq bo'lmagan holda saqlanadi.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SaleOrderDiscountHistoryRecorder {

    private final SaleOrderDiscountHistoryRepository historyRepository;
    private final SaleOrderRepository saleOrderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(SaleOrderDiscountHistoryDTO dto) {
        log.info("Record discount history for sale order {}", dto.getSaleOrderId());

        SaleOrder saleOrder = saleOrderRepository.findById(dto.getSaleOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale order not found with id: " + dto.getSaleOrderId()));

        SaleOrderDiscountHistory history = SaleOrderDiscountHistory.builder()
                .saleOrder(saleOrder)
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .discountAmount(dto.getDiscountAmount())
                .previousDiscountAmount(dto.getPreviousDiscountAmount())
                .originalTotalSum(dto.getOriginalTotalSum())
                .totalSumAfter(dto.getTotalSumAfter())
                .build();

        historyRepository.save(history);
    }
}