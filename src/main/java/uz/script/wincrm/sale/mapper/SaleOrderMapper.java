package uz.script.wincrm.sale.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.dto.SaleOrderDTO;
import uz.script.wincrm.sale.response.SaleOrderResponse;

@Component
public class SaleOrderMapper {

    public SaleOrder toEntity(SaleOrderDTO dto) {
        if (dto == null) {
            return null;
        }
        return SaleOrder.builder()
                .comment(dto.getComment())
                .orderDate(dto.getOrderDate())
                .plannedReadyDate(dto.getPlannedReadyDate())
                .plannedDeliveryDate(dto.getPlannedDeliveryDate())
                .totalSum(dto.getTotalSum())
                .debtSum(dto.getTotalSum())
                .build();
    }

    public void updateEntity(SaleOrder entity, SaleOrderDTO dto) {
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
        if (dto.getOrderDate() != null) {
            entity.setOrderDate(dto.getOrderDate());
        }
        if (dto.getPlannedReadyDate() != null) {
            entity.setPlannedReadyDate(dto.getPlannedReadyDate());
        }
        if (dto.getPlannedDeliveryDate() != null) {
            entity.setPlannedDeliveryDate(dto.getPlannedDeliveryDate());
        }
        if (dto.getTotalSum() != null) {
            entity.setTotalSum(dto.getTotalSum());
        }
    }

    public SaleOrderResponse toResponse(SaleOrder entity) {
        if (entity == null) {
            return null;
        }
        return SaleOrderResponse.builder()
                .id(entity.getId())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .clientFullName(entity.getClient() != null ? entity.getClient().getFullName() : null)
                .warehouseId(entity.getWarehouse() != null ? entity.getWarehouse().getId() : null)
                .warehouseName(entity.getWarehouse() != null ? entity.getWarehouse().getName() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userFullName(entity.getUser() != null ? entity.getUser().getFullName() : null)
                .comment(entity.getComment())
                .orderDate(entity.getOrderDate())
                .plannedReadyDate(entity.getPlannedReadyDate())
                .plannedDeliveryDate(entity.getPlannedDeliveryDate())
                .totalSum(entity.getTotalSum())
                .paidSum(entity.getPaidSum())
                .debtSum(entity.getDebtSum())
                .status(entity.getStatus())
                .orderStatus(entity.getSalesOrderStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedUserId())
                .build();
    }
}