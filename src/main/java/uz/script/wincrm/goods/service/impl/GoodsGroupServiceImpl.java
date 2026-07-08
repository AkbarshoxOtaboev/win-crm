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
import uz.script.wincrm.goods.GoodsGroup;
import uz.script.wincrm.goods.dto.GoodsGroupDTO;
import uz.script.wincrm.goods.response.GoodsGroupResponse;
import uz.script.wincrm.goods.mapper.GoodsGroupMapper;
import uz.script.wincrm.goods.repository.GoodsGroupRepository;
import uz.script.wincrm.goods.service.GoodsGroupService;
import uz.script.wincrm.utils.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsGroupServiceImpl implements GoodsGroupService {

    private final GoodsGroupRepository repository;
    private final GoodsGroupMapper mapper;

    @Override
//    @CachePut(value = "goodsGroups", key = "#result.id")
//    @CacheEvict(value = "goodsGroupList", allEntries = true)
    public GoodsGroupResponse create(GoodsGroupDTO dto) {

        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("Goods Group already exists.");
        }

        GoodsGroup entity = mapper.toEntity(dto);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        entity.setCreatedUsername(username);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
//    @CachePut(value = "goodsGroups", key = "#id")
//    @CacheEvict(value = "goodsGroupList", allEntries = true)
    public GoodsGroupResponse update(Long id, GoodsGroupDTO dto) {

        GoodsGroup entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goods Group not found with id: " + id));

        if (!entity.getName().equalsIgnoreCase(dto.getName())
                && repository.existsByNameIgnoreCase(dto.getName())) {

            throw new BadRequestException("Goods Group already exists.");
        }

        mapper.updateEntity(entity, dto);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
//    @CacheEvict(value = {"goodsGroups", "goodsGroupList"}, allEntries = true)
    public void delete(Long id) {

        GoodsGroup entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goods Group not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "goodsGroups", key = "#id")
    public GoodsGroupResponse getById(Long id) {

        GoodsGroup entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goods Group not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable("goodsGroupList")
    public List<GoodsGroupResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GoodsGroupResponse> getAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

}