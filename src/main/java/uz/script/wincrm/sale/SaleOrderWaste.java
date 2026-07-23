package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.math.BigDecimal;

/**
 * Buyurtma (SaleOrder) bo'yicha ishlab chiqarish jarayonida ortib qolgan (chiqindi/qoldiq)
 * materialni qayd etadi. FAQAT INFORMATIV yozuv - hech qanday Stock, totalSum, debtSum yoki
 * boshqa hisob-kitobga TA'SIR QILMAYDI. Maqsad - qancha material ortib qolganini hisobga olib
 * borish (masalan, keyinchalik qayta ishlatish uchun qancha qoldiq to'plangani ko'rinsin).
 * <p>
 * saleOrderItems'ga o'xshab SaleOrder'ga cascade'siz OneToMany orqali bog'lanadi.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.SALE_ORDER_WASTES)
@SQLRestriction("status <> 'DELETED'")
public class SaleOrderWaste extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id", nullable = false)
    private SaleOrder saleOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    /**
     * Yozuv yaratilgan paytdagi Goods nomining suratkopiyasi (denormalized snapshot).
     * Goods keyinchalik o'chirilsa (soft delete) yoki nomi o'zgartirilsa ham, bu yozuv
     * o'sha paytdagi haqiqiy nom bilan tarixiy jihatdan to'g'ri ko'rinishda qoladi.
     */
    @Column(nullable = false)
    private String goodsName;

    /**
     * Yozuv yaratilgan paytdagi o'lchov birligi nomining suratkopiyasi (masalan "m2", "kg", "dona").
     * goods.getUnitType() orqali olinadi va shu yerga bir marta yoziladi.
     */
    private String unitName;

    /**
     * Ishlab chiqarishdan ortib qolgan material miqdori (goodsName'ning o'lchov birligida).
     */
    @Column(nullable = false)
    private BigDecimal quantity;

    /**
     * Ortib qolgan bo'lakning kengligi. Ixtiyoriy - o'lcham bo'yicha kesib olinadigan
     * materiallar uchun (SaleOrderItem'dagi width/height maydonlariga o'xshab).
     */
    private BigDecimal width;

    /**
     * Ortib qolgan bo'lakning balandligi/uzunligi. Ixtiyoriy.
     */
    private BigDecimal height;

    /**
     * Ixtiyoriy izoh (masalan, qoldiq qayerda saqlanayotgani yoki nima uchun ortib qolgani).
     */
    private String comment;
}