package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;
import uz.script.wincrm.warehouse.Warehouse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = TableName.SALE_ORDER_ITEMS)
@SQLRestriction("status <> 'DELETED'")
public class SaleOrderItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Column(nullable = false)
    private BigDecimal priceCost;

    @Column(nullable = false)
    private BigDecimal priceSelling;

    private BigDecimal width;

    private BigDecimal height;

    @Column(nullable = false)
    private BigDecimal count;
    @Column(nullable = false)
    private LocalDateTime arrivalDate;
}
