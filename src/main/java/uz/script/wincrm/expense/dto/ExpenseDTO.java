package uz.script.wincrm.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Expense DTO", description = "Expense data transfer object for creating and updating")
public class ExpenseDTO {

    @NotNull(message = "Category ID is required")
    @Schema(description = "Expense category ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "Expense amount", example = "150000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @NotNull(message = "Expense date is required")
    @Schema(description = "Expense date", example = "2026-07-09", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate expenseDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Expense description")
    private String description;
}