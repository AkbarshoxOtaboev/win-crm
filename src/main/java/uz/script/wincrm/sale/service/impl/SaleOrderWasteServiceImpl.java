package uz.script.wincrm.sale.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.sale.SaleOrder;
import uz.script.wincrm.sale.SaleOrderWaste;
import uz.script.wincrm.sale.dto.SaleOrderWasteDTO;
import uz.script.wincrm.sale.mapper.SaleOrderWasteMapper;
import uz.script.wincrm.sale.repository.SaleOrderRepository;
import uz.script.wincrm.sale.repository.SaleOrderWasteRepository;
import uz.script.wincrm.sale.response.SaleOrderWasteResponse;
import uz.script.wincrm.sale.response.SaleOrderWasteSummaryResponse;
import uz.script.wincrm.sale.service.SaleOrderWasteService;
import uz.script.wincrm.utils.Status;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SaleOrderWasteServiceImpl implements SaleOrderWasteService {

    private final SaleOrderWasteRepository repository;
    private final SaleOrderWasteMapper mapper;
    private final SaleOrderRepository saleOrderRepository;
    private final GoodsRepository goodsRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entity = "SaleOrderWaste"
    )
    public SaleOrderWasteResponse create(SaleOrderWasteDTO dto) {
        log.info("Record sale order waste. Sale Order ID: {}, Goods ID: {}, Quantity: {}",
                dto.getSaleOrderId(), dto.getGoodsId(), dto.getQuantity());

        SaleOrder saleOrder = saleOrderRepository.findById(dto.getSaleOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found with id: " + dto.getSaleOrderId()));

        Goods goods = goodsRepository.findById(dto.getGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Goods not found with id: " + dto.getGoodsId()));

        SaleOrderWaste entity = mapper.toEntity(dto);
        entity.setSaleOrder(saleOrder);
        entity.setGoods(goods);
        // Yaratilgan paytdagi nom va o'lchov birligi suratkopiyasi - tarixiy aniqlik uchun
        entity.setGoodsName(goods.getName());
        entity.setUnitName(goods.getUnitType() != null ? goods.getUnitType().getName() : null);
        entity.setStatus(Status.ACTIVE);

        // DIQQAT: bu yerda StockService, ClientBalanceService yoki SaleOrder.totalSum/debtSum'ga
        // hech qanday tegilmaydi - bu yozuv faqat INFO sifatida saqlanadi.

        entity = repository.save(entity);
        log.info("Sale order waste recorded successfully. ID: {}", entity.getId());

        return mapper.toResponse(entity);
    }

    @Override
    public SaleOrderWasteResponse findById(Long id) {
        log.info("Fetch sale order waste by id {}", id);

        SaleOrderWaste entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order waste not found with id: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public Page<SaleOrderWasteResponse> fetchAll(Pageable pageable) {
        log.info("Fetch all sale order wastes");

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public List<SaleOrderWasteResponse> fetchBySaleOrderId(Long saleOrderId) {
        log.info("Fetch sale order wastes by sale order id {}", saleOrderId);

        return repository.findAllBySaleOrderId(saleOrderId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public Page<SaleOrderWasteResponse> fetchBySaleOrderIdPaginated(Long saleOrderId, Pageable pageable) {
        log.info("Fetch sale order wastes paginated by sale order id {}", saleOrderId);

        return repository.findAllBySaleOrderId(saleOrderId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<SaleOrderWasteResponse> fetchByGoodsId(Long goodsId, Pageable pageable) {
        log.info("Fetch sale order wastes by goods id {}", goodsId);

        return repository.findByGoodsId(goodsId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "SaleOrderWaste"
    )
    public SaleOrderWasteResponse update(Long id, SaleOrderWasteDTO dto) {
        log.info("Update sale order waste with id {}", id);

        SaleOrderWaste entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order waste not found with id: " + id));

        mapper.updateEntity(entity, dto);

        entity = repository.save(entity);

        return mapper.toResponse(entity);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entity = "SaleOrderWaste"
    )
    public void delete(Long id) {
        log.info("Delete sale order waste with id {}", id);

        SaleOrderWaste entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order waste not found with id: " + id));

        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public BigDecimal calculateTotalBySaleOrderId(Long saleOrderId) {
        log.info("Calculate total waste quantity for sale order {}", saleOrderId);

        return repository.sumQuantityBySaleOrderId(saleOrderId);
    }

    @Override
    public List<SaleOrderWasteSummaryResponse> fetchSummaryBySaleOrderId(Long saleOrderId) {
        log.info("Fetch waste summary grouped by goods for sale order {}", saleOrderId);

        return repository.findSummaryBySaleOrderId(saleOrderId);
    }

    @Override
    public List<SaleOrderWasteSummaryResponse> fetchSummaryGroupedByGoods() {
        log.info("Fetch waste summary grouped by goods across all sale orders");

        return repository.findSummaryGroupedByGoods();
    }
}