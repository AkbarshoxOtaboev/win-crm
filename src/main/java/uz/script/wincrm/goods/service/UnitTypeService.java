package uz.script.wincrm.goods.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.script.wincrm.goods.dto.UnitTypeDTO;
import uz.script.wincrm.goods.response.UnitTypeResponse;

import java.util.List;

public interface UnitTypeService {

    /**
     * Unit Type yaratish
     */
    UnitTypeResponse create(UnitTypeDTO dto);

    /**
     * Unit Type yangilash
     */
    UnitTypeResponse update(Long id, UnitTypeDTO dto);

    /**
     * Unit Type o'chirish
     */
    void delete(Long id);

    /**
     * ID bo'yicha olish
     */
    UnitTypeResponse getById(Long id);

    /**
     * Barcha Unit Typelar
     */
    List<UnitTypeResponse> getAll();

    /**
     * Pagination
     */
    Page<UnitTypeResponse> getAll(Pageable pageable);
}