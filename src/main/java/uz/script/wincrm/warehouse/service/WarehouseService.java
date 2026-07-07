package uz.script.wincrm.warehouse.service;

import uz.script.wincrm.warehouse.dto.WarehouseDTO;
import uz.script.wincrm.warehouse.response.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    boolean existsByName(String name);
    WarehouseResponse create(WarehouseDTO dto);
    WarehouseResponse findById(Long id);
    List<WarehouseResponse> fetchAllWarehouses();
    WarehouseResponse update(Long id, WarehouseDTO dto);
    void delete(Long id);
}
