package uz.script.wincrm.stock.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.stock.StockHistory;
import uz.script.wincrm.stock.enums.StockStatus;
import uz.script.wincrm.stock.mapper.StockHistoryMapper;
import uz.script.wincrm.stock.repository.StockHistoryRepository;
import uz.script.wincrm.stock.response.StockHistoryResponse;
import uz.script.wincrm.stock.service.StockHistoryService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockHistoryServiceImpl implements StockHistoryService {

    private final StockHistoryRepository stockHistoryRepository;
    private final GoodsRepository goodsRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockHistoryMapper stockHistoryMapper;

    @Override
    public StockHistoryResponse findById(Long id) {
        StockHistory history = stockHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock history not found with id: " + id));
        return stockHistoryMapper.toResponse(history);
    }

    @Override
    public List<StockHistoryResponse> fetchAll() {
        return stockHistoryRepository.findAll()
                .stream()
                .map(stockHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockHistoryResponse> fetchByWarehouseId(Long warehouseId) {
        return stockHistoryRepository.findAllByWarehouseId(warehouseId)
                .stream()
                .map(stockHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockHistoryResponse> fetchByGoodsId(Long goodsId) {
        return stockHistoryRepository.findAllByGoodsId(goodsId)
                .stream()
                .map(stockHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockHistoryResponse> fetchByGoodsIdAndWarehouseId(Long goodsId, Long warehouseId) {
        return stockHistoryRepository.findAllByGoodsIdAndWarehouseId(goodsId, warehouseId)
                .stream()
                .map(stockHistoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void recordIn(Long goodsId, Long warehouseId, BigDecimal count, BigDecimal balanceAfter, String comment) {
        saveHistory(goodsId, warehouseId, count, balanceAfter, StockStatus.IN, comment);
    }

    @Override
    @Transactional
    public void recordOut(Long goodsId, Long warehouseId, BigDecimal count, BigDecimal balanceAfter, String comment) {
        saveHistory(goodsId, warehouseId, count, balanceAfter, StockStatus.OUT, comment);
    }

    private void saveHistory(
            Long goodsId,
            Long warehouseId,
            BigDecimal count,
            BigDecimal balanceAfter,
            StockStatus stockStatus,
            String comment
    ) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new EntityNotFoundException("Goods not found with id: " + goodsId));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + warehouseId));

        StockHistory history = StockHistory.builder()
                .goods(goods)
                .warehouse(warehouse)
                .stockStatus(stockStatus)
                .count(count)
                .balanceAfter(balanceAfter)
                .comment(comment)
                .status(Status.ACTIVE)
                .build();

        stockHistoryRepository.save(history);
    }
}