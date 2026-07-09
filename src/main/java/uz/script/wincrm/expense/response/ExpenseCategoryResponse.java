package uz.script.wincrm.expense.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.script.wincrm.utils.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(name = "ExpenseCategory Response", description = "Expense category information returned by the API")
public class ExpenseCategoryResponse {

    @Schema(description = "Unique expense category identifier", example = "1")
    private Long id;

    @Schema(description = "Expense category name", example = "Ijara")
    private String name;

    @Schema(description = "Expense category description")
    private String description;

    @Schema(description = "Current status", example = "ACTIVE")
    private Status status;

    @Schema(description = "Date and time when the category was created", example = "2026-07-09T09:30:15")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the category was last updated", example = "2026-07-09T11:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the user who created the category", example = "1")
    private Long createdBy;
}