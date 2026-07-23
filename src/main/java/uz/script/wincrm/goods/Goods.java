package uz.script.wincrm.goods;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.goods.enums.Type;
import uz.script.wincrm.sale.SaleOrderItem;
import uz.script.wincrm.sale.SaleOrderWaste;
import uz.script.wincrm.stock.Stock;
import uz.script.wincrm.stock.StockHistory;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;
import uz.script.wincrm.warehouse.WarehouseOrderItem;

import java.math.BigDecimal;
import java.util.List;

@Entity
@SQLRestriction("status <> 'DELETED'")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.GOODS)
@Getter
@Setter
@SuperBuilder
public class Goods extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_group_id", nullable = false)
    private GoodsGroup goodsGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_type_id", nullable = false)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private BigDecimal priceCost;

    @Column(nullable = false)
    private BigDecimal priceSelling;


    private String barcode;


    private String photo;

    @OneToMany(mappedBy = "goods")
    private List<WarehouseOrderItem> warehouseOrderItems;

    @OneToMany(mappedBy = "goods")
    private List<SaleOrderItem> saleOrderItems;

    /**
     * Ushbu material bo'yicha barcha buyurtmalarda qayd etilgan ortib qolgan (waste) yozuvlar.
     */
    @OneToMany(mappedBy = "goods")
    private List<SaleOrderWaste> saleOrderWastes;

    @OneToMany(mappedBy = "goods")
    private List<Stock> stocks;

    @OneToMany(mappedBy = "goods")
    private List<StockHistory> stockHistories;

}