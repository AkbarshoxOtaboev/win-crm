package uz.script.wincrm.suppliers.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.script.wincrm.suppliers.SupplierPayment;
import uz.script.wincrm.suppliers.dto.SupplierPaymentFilterDTO;

import java.util.ArrayList;
import java.util.List;

public final class SupplierPaymentSpecification {

    private SupplierPaymentSpecification() {
    }

    public static Specification<SupplierPayment> filter(SupplierPaymentFilterDTO filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSupplierId() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("supplier").get("id"), filter.getSupplierId())
                );
            }

            if (filter.getPaymentTypeId() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("paymentType").get("id"), filter.getPaymentTypeId())
                );
            }

            if (filter.getMinSumm() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("paidSumm"), filter.getMinSumm())
                );
            }

            if (filter.getMaxSumm() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("paidSumm"), filter.getMaxSumm())
                );
            }

            if (filter.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), filter.getStatus())
                );
            }

            if (filter.getFromDate() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("paidDate"), filter.getFromDate())
                );
            }

            if (filter.getToDate() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("paidDate"), filter.getToDate())
                );
            }

            query.orderBy(criteriaBuilder.desc(root.get("paidDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}