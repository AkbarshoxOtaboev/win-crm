package uz.script.wincrm.stock;

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
import uz.script.wincrm.warehouse.Warehouse;

import java.math.BigDecimal;

/**
 * Bitta ombordan ikkinchi omborga mahsulot ko'chirilganini qayd etuvchi tarix yozuvi.
 * Haqiqiy count o'zgarishlari {@link Stock} jadvalida va {@code StockHistory}da
 * (StockService.decreaseStock/increaseStock orqali) amalga oshiriladi;
 * bu entity faqat audit/hisobot maqsadida transfer faktini saqlaydi.
 */
@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.STOCK_TRANSFERS)
@SQLRestriction("status <> 'DELETED'")
public class StockTransfer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_warehouse_id", nullable = false)
    private Warehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_warehouse_id", nullable = false)
    private Warehouse toWarehouse;

    @Column(nullable = false)
    private BigDecimal count;

    @Column(length = 500)
    private String comment;
}
