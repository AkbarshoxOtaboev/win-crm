package uz.script.wincrm.inventory;

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
 * Inventarizatsiya qatori — bitta mahsulot bo'yicha
 * tizimdagi qoldiq (systemCount) va jismonan sanalgan qoldiq (actualCount).
 * difference = actualCount - systemCount (confirm bosqichida hisoblanadi/saqlanadi).
 */
@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.INVENTORY_CHECK_ITEMS)
@SQLRestriction("status <> 'DELETED'")
public class InventoryCheckItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_check_id", nullable = false)
    private InventoryCheck inventoryCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @Column(name = "system_count", nullable = false)
    private BigDecimal systemCount;

    @Column(name = "actual_count")
    private BigDecimal actualCount;

    @Column(name = "difference")
    private BigDecimal difference;

    private String comment;
}