package uz.script.wincrm.expense.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "Expense Response", description = "Expense information returned by the API")
public class ExpenseResponse {

    @Schema(description = "Unique expense identifier", example = "1")
    private Long id;

    @Schema(description = "Expense category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Expense category name", example = "Ijara")
    private String categoryName;

    @Schema(description = "Expense amount", example = "150000.00")
    private BigDecimal amount;

    @Schema(description = "Expense date", example = "2026-07-09")
    private LocalDate expenseDate;

    @Schema(description = "Expense description")
    private String description;

    @Schema(description = "Current status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Date and time when the expense was created", example = "2026-07-09T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the expense was last updated", example = "2026-07-09T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the user who created the expense", example = "1")
    private Long createdBy;
}