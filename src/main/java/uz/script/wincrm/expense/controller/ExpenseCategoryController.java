package uz.script.wincrm.expense.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.expense.dto.ExpenseCategoryDTO;
import uz.script.wincrm.expense.response.ExpenseCategoryResponse;
import uz.script.wincrm.expense.service.ExpenseCategoryService;
import uz.script.wincrm.utils.RestApiResponse;

@RestController
@RequestMapping("/api/expense-categories")
@RequiredArgsConstructor
@Tag(name = "Expense Category REST API", description = "Expense Category CRUD operations")
public class ExpenseCategoryController {

    private final ExpenseCategoryService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_CREATE')")
    @Operation(
            summary = "Create expense category",
            description = "Only users with EXPENSE_CATEGORY_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseCategoryResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody ExpenseCategoryDTO dto) {
        ExpenseCategoryResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<ExpenseCategoryResponse>builder()
                                .message("Expense category successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_VIEW')")
    @Operation(
            summary = "Fetch all expense categories",
            description = "Only users with EXPENSE_CATEGORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchAll(
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<ExpenseCategoryResponse>>builder()
                        .message("All expense categories fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_VIEW')")
    @Operation(
            summary = "Fetch expense category by id",
            description = "Only users with EXPENSE_CATEGORY_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseCategoryResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Expense category ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<ExpenseCategoryResponse>builder()
                        .message("Expense category found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_EDIT')")
    @Operation(
            summary = "Update expense category",
            description = "Only users with EXPENSE_CATEGORY_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseCategoryResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Expense category ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ExpenseCategoryDTO dto
    ) {
        ExpenseCategoryResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<ExpenseCategoryResponse>builder()
                        .message("Expense category successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_DELETE')")
    @Operation(
            summary = "Delete expense category",
            description = "Only users with EXPENSE_CATEGORY_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Expense category ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Expense category successfully deleted")
                        .build()
        );
    }
}