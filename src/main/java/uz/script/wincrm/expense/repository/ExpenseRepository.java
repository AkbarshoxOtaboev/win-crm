package uz.script.wincrm.expense.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.expense.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByCategoryId(Long categoryId, Pageable pageable);

    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.expenseDate BETWEEN :startDate AND :endDate AND e.status <> 'DELETED'")
    BigDecimal getTotalAmountByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.category.id = :categoryId AND e.status <> 'DELETED'")
    BigDecimal getTotalAmountByCategory(@Param("categoryId") Long categoryId);
}