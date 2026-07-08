package uz.script.wincrm.goods.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.GoodsGroup;
import uz.script.wincrm.goods.UnitType;
import uz.script.wincrm.goods.dto.GoodsDTO;
import uz.script.wincrm.goods.mapper.GoodsMapper;
import uz.script.wincrm.goods.repository.GoodsGroupRepository;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.goods.repository.UnitTypeRepository;
import uz.script.wincrm.goods.response.GoodsResponse;
import uz.script.wincrm.goods.service.GoodsService;
import uz.script.wincrm.storage.StorageService;
import uz.script.wincrm.utils.Status;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository repository;
    private final GoodsGroupRepository goodsGroupRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final GoodsMapper mapper;
    private final StorageService storageService;

    @Override
    public boolean existsByBarcode(String barcode) {
        return barcode != null && !barcode.isBlank() && repository.existsByBarcode(barcode);
    }

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Goods"
    )
//    @CacheEvict(value = "goods", allEntries = true)
    public GoodsResponse create(GoodsDTO dto) {
        log.info("Create goods");

        if (existsByBarcode(dto.getBarcode())) {
            throw new AlreadyExistsException(
                    "Goods with barcode '" + dto.getBarcode() + "' already exists");
        }

        if (dto.getPhoto() == null || dto.getPhoto().isEmpty()) {
            throw new IllegalArgumentException("Photo is required");
        }

        GoodsGroup goodsGroup = goodsGroupRepository.findById(dto.getGoodsGroupId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Goods group not found with id: " + dto.getGoodsGroupId()));

        UnitType unitType = unitTypeRepository.findById(dto.getUnitTypeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Unit type not found with id: " + dto.getUnitTypeId()));

        String username = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getName();

        String photoPath = "/api/files/" + storageService.uploadFile(dto.getPhoto());

        Goods goods = mapper.toEntity(dto, goodsGroup, unitType, photoPath);
        goods.setCreatedUsername(username);

        goods = repository.save(goods);

        return mapper.toResponse(goods);
    }

    @Override
//    @Cacheable(value = "good", key = "#id")
    public GoodsResponse findById(Long id) {
        log.info("Fetch goods by id {}", id);

        Goods goods = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goods not found with id: " + id));

        return mapper.toResponse(goods);
    }

    @Override
//    @Cacheable(value = "goods")
    public List<GoodsResponse> fetchAllGoods() {
        log.info("Fetch all goods");

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Goods"
    )
//    @CacheEvict(value = {"goods", "good"}, allEntries = true)
    public GoodsResponse update(Long id, GoodsDTO dto) {
        log.info("Update goods with id {}", id);

        Goods goods = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goods not found with id: " + id));

        if (!goods.getBarcode().equals(dto.getBarcode())
                && existsByBarcode(dto.getBarcode())) {
            throw new AlreadyExistsException(
                    "Goods with barcode '" + dto.getBarcode() + "' already exists");
        }

        GoodsGroup goodsGroup = goodsGroupRepository.findById(dto.getGoodsGroupId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Goods group not found with id: " + dto.getGoodsGroupId()));

        UnitType unitType = unitTypeRepository.findById(dto.getUnitTypeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Unit type not found with id: " + dto.getUnitTypeId()));

        // Photo is optional on update — only re-upload and replace if a new file was provided
        String photoPath = null;
        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            photoPath = "/api/files/"+storageService.uploadFile(dto.getPhoto());
        }

        mapper.updateEntity(goods, dto, goodsGroup, unitType, photoPath);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setCreatedUsername(username);
        goods = repository.save(goods);

        return mapper.toResponse(goods);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Goods"
    )
//    @CacheEvict(value = {"goods", "good"}, allEntries = true)
    public void delete(Long id) {
        log.info("Delete goods with id {}", id);

        Goods goods = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goods not found with id: " + id));

        goods.setStatus(Status.DELETED);
        repository.save(goods);
    }
}