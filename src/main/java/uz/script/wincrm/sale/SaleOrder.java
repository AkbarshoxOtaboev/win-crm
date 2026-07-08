package uz.script.wincrm.sale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.clients.Client;
import uz.script.wincrm.sale.enums.SalesOrderStatus;
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

    private String comment;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private BigDecimal totalSum;

    private BigDecimal paidSum;

    private BigDecimal debtSum;

    @Enumerated(EnumType.STRING)
    private SalesOrderStatus salesOrderStatus;

    @OneToMany(mappedBy = "saleOrder")
    private List<SaleOrderItem> saleOrderItems;
}
