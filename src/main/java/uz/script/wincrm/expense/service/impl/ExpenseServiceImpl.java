package uz.script.wincrm.expense.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.dashboard.responses.DailyExpenseReportResponse;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.expense.Expense;
import uz.script.wincrm.expense.ExpenseCategory;
import uz.script.wincrm.expense.dto.ExpenseDTO;
import uz.script.wincrm.expense.mapper.ExpenseMapper;
import uz.script.wincrm.expense.repository.ExpenseCategoryRepository;
import uz.script.wincrm.expense.repository.ExpenseRepository;
import uz.script.wincrm.expense.response.ExpenseResponse;
import uz.script.wincrm.expense.service.ExpenseService;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;
    private final ExpenseMapper mapper;
    private final ExpenseCategoryRepository categoryRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Expense"
    )
    public ExpenseResponse create(ExpenseDTO dto) {
        log.info("Create expense for category {}", dto.getCategoryId());

        ExpenseCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense category not found with id: " + dto.getCategoryId()));

        Expense entity = mapper.toEntity(dto);
        entity.setCategory(category);
        entity.setStatus(Status.ACTIVE);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    public ExpenseResponse findById(Long id) {
        log.info("Fetch expense by id {}", id);

        Expense entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public Page<ExpenseResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all expenses");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<ExpenseResponse> fetchByCategoryId(Long categoryId, Pageable pageable) {
        log.info("Fetch expenses by category id {}", categoryId);

        return repository.findByCategoryId(categoryId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public List<ExpenseResponse> fetchByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetch expenses by date range {} - {}", startDate, endDate);

        return repository.findByExpenseDateBetween(startDate, endDate)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Page<ExpenseResponse> fetchByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.info("Fetch expenses (paginated) by date range {} - {}", startDate, endDate);

        return repository.findByExpenseDateBetween(startDate, endDate, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Expense"
    )
    public ExpenseResponse update(Long id, ExpenseDTO dto) {
        log.info("Update expense with id {}", id);

        Expense entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        if (dto.getCategoryId() != null
                && (entity.getCategory() == null || !dto.getCategoryId().equals(entity.getCategory().getId()))) {
            ExpenseCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Expense category not found with id: " + dto.getCategoryId()));
            entity.setCategory(category);
        }

        mapper.updateEntity(entity, dto);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Expense"
    )
    public void delete(Long id) {
        log.info("Delete expense with id {}", id);

        Expense entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public List<DailyExpenseReportResponse> findExpenseReportByExpenesCategory(
            LocalDate startDate,
            LocalDate endDate
    ) {

        List<Expense> expenses = repository.findExpenseReportByExpenesCategory(startDate, endDate);

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(list -> {

                    ExpenseCategory category = list.getFirst().getCategory();

                    return DailyExpenseReportResponse.builder()
                            .categoryId(category.getId())
                            .categoryName(category.getName())
                            .totalAmount(
                                    list.stream()
                                            .map(Expense::getAmount)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            )
                            .build();
                })
                .toList();
    }
}