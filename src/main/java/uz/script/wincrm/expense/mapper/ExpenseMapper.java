package uz.script.wincrm.expense.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.expense.Expense;
import uz.script.wincrm.expense.dto.ExpenseDTO;
import uz.script.wincrm.expense.response.ExpenseResponse;

@Component
public class ExpenseMapper {

    public Expense toEntity(ExpenseDTO dto) {
        if (dto == null) {
            return null;
        }
        return Expense.builder()
                .amount(dto.getAmount())
                .expenseDate(dto.getExpenseDate())
                .description(dto.getDescription())
                .build();
    }

    public void updateEntity(Expense entity, ExpenseDTO dto) {
        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }
        if (dto.getExpenseDate() != null) {
            entity.setExpenseDate(dto.getExpenseDate());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }

    public ExpenseResponse toResponse(Expense entity) {
        if (entity == null) {
            return null;
        }
        return ExpenseResponse.builder()
                .id(entity.getId())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .amount(entity.getAmount())
                .expenseDate(entity.getExpenseDate())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedUserId())
                .build();
    }
}