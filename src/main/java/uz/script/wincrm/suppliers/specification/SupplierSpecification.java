package uz.script.wincrm.suppliers.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.dto.SupplierFilterDTO;

import java.util.ArrayList;
import java.util.List;

public final class SupplierSpecification {

    private SupplierSpecification() {
    }

    public static Specification<Supplier> filter(SupplierFilterDTO filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + filter.getName().trim().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getInn() != null && !filter.getInn().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("inn")),
                                "%" + filter.getInn().trim().toLowerCase() + "%"
                        )
                );
            }


            if (filter.getPhone() != null && !filter.getPhone().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("phone")),
                                "%" + filter.getPhone().trim().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), filter.getStatus())
                );
            }

            if (filter.getFromDate() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getFromDate()
                        )
                );
            }

            if (filter.getToDate() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getToDate()
                        )
                );
            }

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}