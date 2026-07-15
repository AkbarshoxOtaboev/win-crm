package uz.script.wincrm.expense.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.dashboard.responses.DailyExpenseReportResponse;
import uz.script.wincrm.expense.dto.ExpenseDTO;
import uz.script.wincrm.expense.response.ExpenseResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse create(ExpenseDTO dto);

    ExpenseResponse findById(Long id);

    Page<ExpenseResponse> fetchAll(Pageable pageable);

    Page<ExpenseResponse> fetchByCategoryId(Long categoryId, Pageable pageable);

    List<ExpenseResponse> fetchByDateRange(LocalDate startDate, LocalDate endDate);

    Page<ExpenseResponse> fetchByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    ExpenseResponse update(Long id, ExpenseDTO dto);

    void delete(Long id);

    List<DailyExpenseReportResponse> findExpenseReportByExpenesCategory(LocalDate startDate, LocalDate endDate);
}