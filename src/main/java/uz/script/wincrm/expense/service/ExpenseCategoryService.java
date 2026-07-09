package uz.script.wincrm.expense.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.expense.dto.ExpenseCategoryDTO;
import uz.script.wincrm.expense.response.ExpenseCategoryResponse;

public interface ExpenseCategoryService {

    ExpenseCategoryResponse create(ExpenseCategoryDTO dto);

    ExpenseCategoryResponse findById(Long id);

    Page<ExpenseCategoryResponse> fetchAll(Pageable pageable);

    ExpenseCategoryResponse update(Long id, ExpenseCategoryDTO dto);

    void delete(Long id);
}