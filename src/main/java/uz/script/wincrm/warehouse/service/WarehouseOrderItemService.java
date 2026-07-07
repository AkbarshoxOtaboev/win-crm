package uz.script.wincrm.warehouse.service;

import uz.script.wincrm.warehouse.dto.WarehouseOrderItemDTO;
import uz.script.wincrm.warehouse.response.WarehouseOrderItemResponse;

import java.util.List;

public interface WarehouseOrderItemService {
    WarehouseOrderItemResponse create(WarehouseOrderItemDTO dto);
    WarehouseOrderItemResponse findById(Long id);
    List<WarehouseOrderItemResponse> fetchAllItems();
    List<WarehouseOrderItemResponse> fetchByWarehouseOrderId(Long warehouseOrderId);
    WarehouseOrderItemResponse update(Long id, WarehouseOrderItemDTO dto);
    void delete(Long id);
}
