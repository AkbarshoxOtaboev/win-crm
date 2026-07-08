package uz.script.wincrm.stock.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.stock.Stock;
import uz.script.wincrm.stock.mapper.StockMapper;
import uz.script.wincrm.stock.repository.StockRepository;
import uz.script.wincrm.stock.response.StockResponse;
import uz.script.wincrm.stock.service.StockHistoryService;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final GoodsRepository goodsRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockMapper stockMapper;
    private final StockHistoryService stockHistoryService;

    @Override
    public StockResponse findById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));
        return stockMapper.toResponse(stock);
    }

    @Override
    public List<StockResponse> fetchAll() {
        return stockRepository.findAll()
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockResponse> fetchByWarehouseId(Long warehouseId) {
        return stockRepository.findAllByWarehouseId(warehouseId)
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockResponse> fetchByGoodsId(Long goodsId) {
        return stockRepository.findAllByGoodsId(goodsId)
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void increaseStock(Long goodsId, Long warehouseId, BigDecimal count) {
        Stock stock = stockRepository.findByGoodsIdAndWarehouseId(goodsId, warehouseId)
                .map(existing -> {
                    existing.setCount(existing.getCount().add(count));
                    return stockRepository.save(existing);
                })
                .orElseGet(() -> {
                    Goods goods = goodsRepository.findById(goodsId)
                            .orElseThrow(() -> new EntityNotFoundException("Goods not found with id: " + goodsId));
                    Warehouse warehouse = warehouseRepository.findById(warehouseId)
                            .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + warehouseId));

                    Stock newStock = Stock.builder()
                            .goods(goods)
                            .warehouse(warehouse)
                            .count(count)
                            .status(Status.ACTIVE)
                            .build();
                    return stockRepository.save(newStock);
                });

        stockHistoryService.recordIn(goodsId, warehouseId, count, stock.getCount(), "Omborga mahsulot kirim qilindi");
    }

    @Override
    public BigDecimal getAvailableStock(Long goodsId, Long warehouseId) {
        return stockRepository.findByGoodsIdAndWarehouseId(goodsId, warehouseId)
                .map(Stock::getCount)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public void decreaseStock(Long goodsId, Long warehouseId, BigDecimal count) {
        Stock stock = stockRepository.findByGoodsIdAndWarehouseId(goodsId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Stock not found for goodsId: " + goodsId + " and warehouseId: " + warehouseId));

        if (stock.getCount().compareTo(count) < 0) {
            throw new IllegalArgumentException("Insufficient stock count for goodsId: " + goodsId);
        }

        stock.setCount(stock.getCount().subtract(count));
        stockRepository.save(stock);

        stockHistoryService.recordOut(goodsId, warehouseId, count, stock.getCount(), "Ombordan mahsulot chiqim qilindi");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));
        stock.setStatus(Status.DELETED);
        stockRepository.save(stock);
    }
}