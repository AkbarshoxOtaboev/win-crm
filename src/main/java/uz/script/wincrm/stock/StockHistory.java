package uz.script.wincrm.stock;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.stock.enums.StockStatus;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;
import uz.script.wincrm.warehouse.Warehouse;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.STOCK_HISTORIES)
@SQLRestriction("status <> 'DELETED'")
public class StockHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false)
    private StockStatus stockStatus;

    @Column(nullable = false)
    private BigDecimal count;

    @Column(name = "balance_after", nullable = false)
    private BigDecimal balanceAfter;

    private String comment;
}