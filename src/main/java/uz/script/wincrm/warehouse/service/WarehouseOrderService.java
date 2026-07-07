package uz.script.wincrm.warehouse.service;

import uz.script.wincrm.warehouse.dto.WarehouseOrderDTO;
import uz.script.wincrm.warehouse.response.WarehouseOrderResponse;

import java.util.List;

public interface WarehouseOrderService {
    WarehouseOrderResponse create(WarehouseOrderDTO dto);
    WarehouseOrderResponse findById(Long id);
    List<WarehouseOrderResponse> fetchAllOrders();
    List<WarehouseOrderResponse> fetchByWarehouseId(Long warehouseId);
    List<WarehouseOrderResponse> fetchBySupplierId(Long supplierId);
    WarehouseOrderResponse update(Long id, WarehouseOrderDTO dto);
    void delete(Long id);
}
