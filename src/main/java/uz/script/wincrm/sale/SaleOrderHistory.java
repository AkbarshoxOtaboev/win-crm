package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
import uz.script.wincrm.users.User;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

/**
 * SaleOrder statusining har bir o'zgarishini alohida qator sifatida saqlaydi.
 * "changedAt" uchun alohida maydon yo'q - BaseEntity.createdAt shu vazifani bajaradi,
 * chunki har bir qator = "shu status qachon o'rnatilgani" degan bitta voqea.
 *
 * DIQQAT: TableName klassiga quyidagi konstantani qo'shing:
 * public static final String SALE_ORDER_HISTORY = "sale_order_history";
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.SALE_ORDER_HISTORY)
@SQLRestriction("status <> 'DELETED'")
public class SaleOrderHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id", nullable = false)
    private SaleOrder saleOrder;

    /**
     * Eski status. Buyurtma birinchi marta yaratilganda (NEW holatiga o'tganda) null bo'ladi -
     * chunki "hech narsadan" yangi buyurtmaga o'tish bor.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SalesOrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SalesOrderStatus toStatus;

    /**
     * Statusni o'zgartirgan xodim. Agar tizim tomonidan (masalan scheduled job orqali)
     * avtomatik o'zgartirilsa va authentication mavjud bo'lmasa - null bo'lishi mumkin.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

    @Column(columnDefinition = "TEXT")
    private String comment;
}