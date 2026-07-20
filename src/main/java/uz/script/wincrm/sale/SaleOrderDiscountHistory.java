package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uz.script.wincrm.sale.enums.DiscountType;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.math.BigDecimal;

/**
 * Buyurtmaga qo'llangan har bir chegirmaning IMMUTABLE audit yozuvi.
 * SaleOrderHistory (status tarixi) bilan bir xil pattern: faqat INSERT, hech qachon UPDATE emas.
 * Har safar create paytida yoki keyin applyDiscount() chaqirilganda bitta yozuv tushadi -
 * shu tufayli buyurtmaga qachon, kim, qanday chegirma bergani to'liq saqlanib qoladi.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.SALE_ORDER_DISCOUNT_HISTORY)
public class SaleOrderDiscountHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id", nullable = false)
    private SaleOrder saleOrder;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    /**
     * Kiritilgan xom qiymat (foiz yoki summa).
     */
    private BigDecimal discountValue;

    /**
     * Shu o'zgarishda qo'llangan aniq chegirma summasi (yangi qiymat).
     */
    @Column(nullable = false)
    private BigDecimal discountAmount;

    /**
     * O'zgarishdan OLDINGI chegirma summasi. Birinchi marta bo'lsa BigDecimal.ZERO.
     */
    private BigDecimal previousDiscountAmount;

    /**
     * Chegirma qo'llangan paytdagi asl summa (kontekst uchun).
     */
    private BigDecimal originalTotalSum;

    /**
     * Chegirma qo'llangandan keyingi yakuniy totalSum.
     */
    private BigDecimal totalSumAfter;
}