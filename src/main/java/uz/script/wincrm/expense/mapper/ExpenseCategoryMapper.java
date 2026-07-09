package uz.script.wincrm.expense.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.expense.ExpenseCategory;
import uz.script.wincrm.expense.dto.ExpenseCategoryDTO;
import uz.script.wincrm.expense.response.ExpenseCategoryResponse;

@Component
public class ExpenseCategoryMapper {

    public ExpenseCategory toEntity(ExpenseCategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        return ExpenseCategory.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public void updateEntity(ExpenseCategory entity, ExpenseCategoryDTO dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }

    public ExpenseCategoryResponse toResponse(ExpenseCategory entity) {
        if (entity == null) {
            return null;
        }
        return ExpenseCategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedUserId())
                .build();
    }
}