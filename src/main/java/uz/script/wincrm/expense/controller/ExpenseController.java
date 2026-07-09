package uz.script.wincrm.expense.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.expense.dto.ExpenseDTO;
import uz.script.wincrm.expense.response.ExpenseResponse;
import uz.script.wincrm.expense.service.ExpenseService;
import uz.script.wincrm.utils.RestApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expense REST API", description = "Expense CRUD operations")
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('EXPENSE_CREATE')")
    @Operation(
            summary = "Create expense",
            description = "Only users with EXPENSE_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody ExpenseDTO dto) {
        ExpenseResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<ExpenseResponse>builder()
                                .message("Expense successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(
            summary = "Fetch all expenses",
            description = "Only users with EXPENSE_VIEW permission can use this endpoint."
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
                RestApiResponse.<Page<ExpenseResponse>>builder()
                        .message("All expenses fetched successfully")
                        .data(service.fetchAll(pageable))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(
            summary = "Fetch expense by id",
            description = "Only users with EXPENSE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Expense ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<ExpenseResponse>builder()
                        .message("Expense found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(
            summary = "Fetch expenses by category",
            description = "Only users with EXPENSE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByCategoryId(
            @Parameter(description = "Expense category ID", example = "1")
            @PathVariable Long categoryId,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<ExpenseResponse>>builder()
                        .message("Expenses for category fetched successfully")
                        .data(service.fetchByCategoryId(categoryId, pageable))
                        .build()
        );
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(
            summary = "Fetch expenses by date range",
            description = "Only users with EXPENSE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ExpenseResponse.class))
            )
    )
    public ResponseEntity<?> fetchByDateRange(
            @Parameter(description = "Start date", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<List<ExpenseResponse>>builder()
                        .message("Expenses for date range fetched successfully")
                        .data(service.fetchByDateRange(startDate, endDate))
                        .build()
        );
    }

    @GetMapping("/date-range/paginated")
    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @Operation(
            summary = "Fetch expenses by date range (paginated)",
            description = "Only users with EXPENSE_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<?> fetchByDateRangePaginated(
            @Parameter(description = "Start date", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<Page<ExpenseResponse>>builder()
                        .message("Expenses for date range fetched successfully")
                        .data(service.fetchByDateRange(startDate, endDate, pageable))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_EDIT')")
    @Operation(
            summary = "Update expense",
            description = "Only users with EXPENSE_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Expense ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDTO dto
    ) {
        ExpenseResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<ExpenseResponse>builder()
                        .message("Expense successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_DELETE')")
    @Operation(
            summary = "Delete expense",
            description = "Only users with EXPENSE_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Expense ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Expense successfully deleted")
                        .build()
        );
    }
}