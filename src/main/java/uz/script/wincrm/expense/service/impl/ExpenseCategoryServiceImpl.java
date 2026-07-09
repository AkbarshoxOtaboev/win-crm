package uz.script.wincrm.expense.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.expense.ExpenseCategory;
import uz.script.wincrm.expense.dto.ExpenseCategoryDTO;
import uz.script.wincrm.expense.mapper.ExpenseCategoryMapper;
import uz.script.wincrm.expense.repository.ExpenseCategoryRepository;
import uz.script.wincrm.expense.response.ExpenseCategoryResponse;
import uz.script.wincrm.expense.service.ExpenseCategoryService;
import uz.script.wincrm.utils.Status;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;
    private final ExpenseCategoryMapper mapper;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "ExpenseCategory"
    )
    public ExpenseCategoryResponse create(ExpenseCategoryDTO dto) {
        log.info("Create expense category {}", dto.getName());

        repository.findByNameIgnoreCase(dto.getName())
                .ifPresent(existing -> {
                    throw new AlreadyExistsException("Expense category already exists with name: " + dto.getName());
                });

        ExpenseCategory entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    public ExpenseCategoryResponse findById(Long id) {
        log.info("Fetch expense category by id {}", id);

        ExpenseCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense category not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public Page<ExpenseCategoryResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all expense categories");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "ExpenseCategory"
    )
    public ExpenseCategoryResponse update(Long id, ExpenseCategoryDTO dto) {
        log.info("Update expense category with id {}", id);

        ExpenseCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense category not found with id: " + id));

        if (dto.getName() != null && !dto.getName().equalsIgnoreCase(entity.getName())) {
            repository.findByNameIgnoreCase(dto.getName())
                    .ifPresent(existing -> {
                        throw new AlreadyExistsException("Expense category already exists with name: " + dto.getName());
                    });
        }

        mapper.updateEntity(entity, dto);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "ExpenseCategory"
    )
    public void delete(Long id) {
        log.info("Delete expense category with id {}", id);

        ExpenseCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense category not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }
}