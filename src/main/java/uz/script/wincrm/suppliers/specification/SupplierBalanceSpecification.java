package uz.script.wincrm.suppliers.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.SupplierBalance;
import uz.script.wincrm.suppliers.dto.SupplierBalanceFilterDTO;

import java.util.ArrayList;
import java.util.List;

public final class SupplierBalanceSpecification {

    private SupplierBalanceSpecification() {
    }

    public static Specification<SupplierBalance> filter(SupplierBalanceFilterDTO filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            Join<SupplierBalance, Supplier> supplierJoin = root.join("supplier");

            if (filter.getSupplierName() != null && !filter.getSupplierName().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(supplierJoin.get("name")),
                                "%" + filter.getSupplierName().trim().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getMinDebt() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("totalDebt"), filter.getMinDebt())
                );
            }

            if (filter.getMaxDebt() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("totalDebt"), filter.getMaxDebt())
                );
            }

            if (filter.getFromDate() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("lastUpdated"), filter.getFromDate())
                );
            }

            if (filter.getToDate() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("lastUpdated"), filter.getToDate())
                );
            }

            query.orderBy(criteriaBuilder.desc(root.get("lastUpdated")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}