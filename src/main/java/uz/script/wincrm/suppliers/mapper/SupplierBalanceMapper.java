package uz.script.wincrm.suppliers.mapper;

import lombok.experimental.UtilityClass;
import uz.script.wincrm.suppliers.SupplierBalance;
import uz.script.wincrm.suppliers.response.SupplierBalanceResponse;

@UtilityClass
public class SupplierBalanceMapper {

    public SupplierBalanceResponse toResponse(SupplierBalance balance) {
        if (balance == null) {
            return null;
        }

        return SupplierBalanceResponse.builder()
                .id(balance.getId())
                .supplierId(balance.getSupplier() != null ? balance.getSupplier().getId() : null)
                .supplierName(balance.getSupplier() != null ? balance.getSupplier().getName() : null)
                .totalPurchase(balance.getTotalPurchase())
                .totalPaid(balance.getTotalPaid())
                .totalDebt(balance.getTotalDebt())
                .lastUpdated(balance.getLastUpdated())
                .status(balance.getStatus())
                .createdAt(balance.getCreatedAt())
                .updatedAt(balance.getUpdatedAt())
                .build();
    }
}