package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.payment.Payment;
import uz.script.wincrm.sale.enums.DiscountType;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
import uz.script.wincrm.users.User;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;
import uz.script.wincrm.warehouse.Warehouse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.SALE_ORDERS)
@SQLRestriction("status <> 'DELETED'")
public class SaleOrder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String comment;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    /**
     * Buyurtmani yaratgan xodim tomonidan (odatda create paytida) qo'lda belgilanadigan,
     * "buyurtma qachon tayyor bo'lishi rejalashtirilgani" sanasi. Statusdan mustaqil -
     * avtomatik o'rnatilmaydi, faqat kiritilgan qiymat saqlanadi.
     */
    private LocalDateTime plannedReadyDate;

    /**
     * Buyurtmani yaratgan xodim tomonidan qo'lda belgilanadigan,
     * "mijozga qachon yetkazilishi rejalashtirilgani" sanasi. "Vaqtida yetkazilyaptimi"
     * nazoratini yuritish uchun ishlatiladi (haqiqiy DELIVERED bo'lgan vaqt bilan solishtiriladi).
     */
    private LocalDateTime plannedDeliveryDate;

    /**
     * Chegirmagacha bo'lgan ASL buyurtma summasi. Frontend qanday hisoblab yuborgan bo'lsa
     * (itemlardan yig'ib yoki to'g'ridan-to'g'ri), o'sha qiymat shu maydonga tushadi.
     *
     * DIQQAT: bu maydon chegirma hisoblash uchun O'ZGARMAS tayanch nuqta bo'lib xizmat qiladi.
     * Chegirma bir necha marta qayta qo'llansa ham, har safar totalSum shu asl qiymatdan
     * qayta hisoblanadi - shu tufayli chegirma ustma-ust tushib totalSum noto'g'ri
     * kamayib ketmaydi.
     */
    @Column(nullable = false)
    private BigDecimal originalTotalSum;

    /**
     * Qo'llangan chegirma turi (foizli yoki aniq summa). Chegirma yo'q bo'lsa null.
     */
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    /**
     * Foydalanuvchi kiritgan XOM chegirma qiymati:
     * - PERCENTAGE bo'lsa: foiz (masalan 10)
     * - FIXED_AMOUNT bo'lsa: summa (masalan 50000)
     */
    private BigDecimal discountValue;

    /**
     * Hisoblangan ANIQ chegirma summasi (pul birligida). originalTotalSum'dan
     * discountType/discountValue asosida bir marta hisoblanib SHU YERGA YOZILADI.
     * Keyin qayta hisoblanmaydi - buyurtmaga qachon qanday chegirma berilgani aynan
     * shu qiymatda saqlanib qoladi.
     */
    private BigDecimal discountAmount;

    /**
     * Mijozga to'lash uchun qoladigan YAKUNIY summa (chegirma qo'llangandan keyin).
     * Formula: totalSum = originalTotalSum - discountAmount.
     * SaleOrderItem hisob-kitobi bu formulaga ARALASHMAYDI.
     */
    @Column(nullable = false)
    private BigDecimal totalSum;

    private BigDecimal paidSum;

    private BigDecimal debtSum;

    @Enumerated(EnumType.STRING)
    private SalesOrderStatus salesOrderStatus;

    @OneToMany(mappedBy = "saleOrder")
    private List<SaleOrderItem> saleOrderItems;

    /**
     * Ishlab chiqarishdan ortib qolgan materiallar (waste) ro'yxati. saleOrderItems'ga
     * o'xshab cascade'siz - faqat INFO/hisobot maqsadida o'qiladi, buyurtma hisob-kitobiga
     * (totalSum/debtSum) hech qanday ta'sir qilmaydi.
     */
    @OneToMany(mappedBy = "saleOrder")
    private List<SaleOrderWaste> saleOrderWastes;

    @OneToMany(mappedBy = "saleOrder")
    private List<Payment> payments;
}