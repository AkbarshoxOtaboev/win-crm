package uz.script.wincrm.sale.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.sale.enums.DiscountType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class SaleOrderDiscountHistoryDTO {

    private Long saleOrderId;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private BigDecimal previousDiscountAmount;
    private BigDecimal originalTotalSum;
    private BigDecimal totalSumAfter;
}