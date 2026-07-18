package uz.script.wincrm.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import uz.script.wincrm.inventory.enums.InventoryCheckStatus;
import uz.script.wincrm.utils.BaseEntity;
import uz.script.wincrm.utils.TableName;
import uz.script.wincrm.warehouse.Warehouse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventarizatsiya bosh hujjati (sessiyasi).
 * Bitta ombor uchun bitta sanoq jarayonini ifodalaydi.
 * Boshlanganda shu ombordagi barcha Stock qoldiqlari "muzlatib"
 * InventoryCheckItem sifatida saqlanadi (systemCount).
 */
@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = TableName.INVENTORY_CHECKS)
@SQLRestriction("status <> 'DELETED'")
public class InventoryCheck extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    /**
     * Diqqat: bu maydon atayin "checkStatus" deb nomlangan,
     * chunki BaseEntity'da soft-delete uchun "status" maydoni allaqachon mavjud.
     */
    @Column(name = "check_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InventoryCheckStatus checkStatus = InventoryCheckStatus.IN_PROGRESS;

    private String comment;

    private LocalDateTime confirmedAt;

    private Long confirmedUserId;

    private String confirmedUsername;

    @OneToMany(mappedBy = "inventoryCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryCheckItem> items = new ArrayList<>();
}