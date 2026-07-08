package uz.script.wincrm.warehouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.SaleOrderItem;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SQLRestriction("status <> 'DELETED'")
@Table(name = TableName.WAREHOUSE)
public class Warehouse extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "warehouse")
    private List<WarehouseOrder> warehouseOrders;

    @OneToMany(mappedBy = "warehouse")
    private List<SaleOrder> saleOrders;

    @OneToMany(mappedBy = "warehouse")
    private List<SaleOrderItem> saleOrderItems;

    @OneToMany(mappedBy = "warehouse")
    private  List<WarehouseOrderItem> warehouseOrderItems;
}
