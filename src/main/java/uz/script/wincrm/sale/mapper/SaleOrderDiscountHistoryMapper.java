package uz.script.wincrm.sale.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.sale.SaleOrderDiscountHistory;
import uz.script.wincrm.sale.response.SaleOrderDiscountHistoryResponse;

@Component
public class SaleOrderDiscountHistoryMapper {

    public SaleOrderDiscountHistoryResponse toResponse(SaleOrderDiscountHistory entity) {
        if (entity == null) {
            return null;
        }
        return SaleOrderDiscountHistoryResponse.builder()
                .id(entity.getId())
                .saleOrderId(entity.getSaleOrder() != null ? entity.getSaleOrder().getId() : null)
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .discountAmount(entity.getDiscountAmount())
                .previousDiscountAmount(entity.getPreviousDiscountAmount())
                .originalTotalSum(entity.getOriginalTotalSum())
                .totalSumAfter(entity.getTotalSumAfter())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedUserId())
                .build();
    }
}