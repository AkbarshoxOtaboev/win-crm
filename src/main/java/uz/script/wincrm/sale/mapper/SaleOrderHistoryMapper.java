package uz.script.wincrm.sale.mapper;

import uz.script.wincrm.sale.SaleOrderHistory;
import uz.script.wincrm.sale.response.SaleOrderHistoryResponse;

public class SaleOrderHistoryMapper {

    private SaleOrderHistoryMapper() {
    }

    public static SaleOrderHistoryResponse toResponse(SaleOrderHistory history) {
        return SaleOrderHistoryResponse.builder()
                .id(history.getId())
                .saleOrderId(history.getSaleOrder().getId())
                .fromStatus(history.getFromStatus())
                .toStatus(history.getToStatus())
                .changedByUserId(history.getChangedByUser() != null ? history.getChangedByUser().getId() : null)
                .changedByUserFullName(history.getChangedByUser() != null ? history.getChangedByUser().getFullName() : null)
                .comment(history.getComment())
                .changedAt(history.getCreatedAt())
                .build();
    }
}