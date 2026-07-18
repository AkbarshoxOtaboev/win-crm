package uz.script.wincrm.stock.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.script.wincrm.goods.Goods;
import uz.script.wincrm.goods.repository.GoodsRepository;
import uz.script.wincrm.stock.StockTransfer;
import uz.script.wincrm.stock.mapper.StockTransferMapper;
import uz.script.wincrm.stock.repository.StockTransferRepository;
import uz.script.wincrm.stock.request.StockTransferRequest;
import uz.script.wincrm.stock.response.StockTransferResponse;
import uz.script.wincrm.stock.service.StockService;
import uz.script.wincrm.stock.service.StockTransferService;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockTransferServiceImpl implements StockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final StockTransferMapper stockTransferMapper;
    private final StockService stockService;
    private final GoodsRepository goodsRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public StockTransferResponse transfer(StockTransferRequest request) {

        if (request.getFromWarehouseId().equals(request.getToWarehouseId())) {
            throw new IllegalArgumentException("fromWarehouseId va toWarehouseId bir xil bo'lishi mumkin emas");
        }

        Goods goods = goodsRepository.findById(request.getGoodsId())
                .orElseThrow(() -> new EntityNotFoundException("Goods not found with id: " + request.getGoodsId()));

        Warehouse fromWarehouse = warehouseRepository.findById(request.getFromWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + request.getFromWarehouseId()));

        Warehouse toWarehouse = warehouseRepository.findById(request.getToWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with id: " + request.getToWarehouseId()));

        // Stock jadvali - yagona ishonch manbai (memory: "Stock as single source of truth")
        BigDecimal available = stockService.getAvailableStock(request.getGoodsId(), request.getFromWarehouseId());
        if (available.compareTo(request.getCount()) < 0) {
            throw new IllegalArgumentException(
                    "Manba ombordagi mahsulot yetarli emas. Mavjud: " + available + ", so'ralgan: " + request.getCount());
        }

        // Manba ombordan chiqim -> StockHistory OUT avtomatik yoziladi
        stockService.decreaseStock(request.getGoodsId(), request.getFromWarehouseId(), request.getCount());

        // Maqsad omborga kirim -> StockHistory IN avtomatik yoziladi
        stockService.increaseStock(request.getGoodsId(), request.getToWarehouseId(), request.getCount());

        StockTransfer transfer = StockTransfer.builder()
                .goods(goods)
                .fromWarehouse(fromWarehouse)
                .toWarehouse(toWarehouse)
                .count(request.getCount())
                .comment(request.getComment())
                .status(Status.ACTIVE)
                .build();

        StockTransfer saved = stockTransferRepository.save(transfer);

        return stockTransferMapper.toResponse(saved);
    }

    @Override
    public StockTransferResponse findById(Long id) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock transfer not found with id: " + id));
        return stockTransferMapper.toResponse(transfer);
    }

    @Override
    public List<StockTransferResponse> fetchAll() {
        return stockTransferRepository.findAll()
                .stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockTransferResponse> fetchByWarehouseId(Long warehouseId) {
        return stockTransferRepository.findAllByFromWarehouseIdOrToWarehouseId(warehouseId, warehouseId)
                .stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Override
    public List<StockTransferResponse> fetchByGoodsId(Long goodsId) {
        return stockTransferRepository.findAllByGoodsId(goodsId)
                .stream()
                .map(stockTransferMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock transfer not found with id: " + id));
        transfer.setStatus(Status.DELETED);
        stockTransferRepository.save(transfer);
    }
}
