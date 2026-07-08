package uz.script.wincrm.goods.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.goods.UnitType;
import uz.script.wincrm.goods.dto.UnitTypeDTO;
import uz.script.wincrm.goods.response.UnitTypeResponse;
import uz.script.wincrm.goods.mapper.UnitTypeMapper;
import uz.script.wincrm.goods.repository.UnitTypeRepository;
import uz.script.wincrm.goods.service.UnitTypeService;
import uz.script.wincrm.utils.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UnitTypeServiceImpl implements UnitTypeService {

    private final UnitTypeRepository repository;
    private final UnitTypeMapper mapper;

    /**
     * Unit Type yaratish
     */
    @Override
//    @CachePut(value = "unitTypes", key = "#result.id")
//    @CacheEvict(value = "unitTypeList", allEntries = true)
    public UnitTypeResponse create(UnitTypeDTO dto) {

        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("Unit Type already exists.");
        }

        UnitType entity = mapper.toEntity(dto);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        entity.setCreatedUsername(username);
        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    /**
     * Unit Type yangilash
     */
    @Override
//    @CachePut(value = "unitTypes", key = "#id")
//    @CacheEvict(value = "unitTypeList", allEntries = true)
    public UnitTypeResponse update(Long id, UnitTypeDTO dto) {

        UnitType entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit Type not found with id: " + id));

        if (!entity.getName().equalsIgnoreCase(dto.getName())
                && repository.existsByNameIgnoreCase(dto.getName())) {

            throw new BadRequestException("Unit Type already exists.");
        }

        mapper.updateEntity(entity, dto);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    /**
     * Unit Type o'chirish
     */
    @Override
//    @CacheEvict(value = {"unitTypes", "unitTypeList"}, key = "#id", allEntries = true)
    public void delete(Long id) {

        UnitType entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit Type not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    /**
     * ID bo'yicha olish
     */
    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "unitTypes", key = "#id")
    public UnitTypeResponse getById(Long id) {

        UnitType entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Unit Type not found with id: " + id));

        return mapper.toResponse(entity);
    }

    /**
     * Barchasini olish
     */
    @Override
    @Transactional(readOnly = true)
//    @Cacheable("unitTypeList")
    public List<UnitTypeResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Pagination
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnitTypeResponse> getAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

}