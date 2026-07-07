package uz.script.wincrm.warehouse.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.utils.Status;
import uz.script.wincrm.warehouse.Warehouse;
import uz.script.wincrm.warehouse.dto.WarehouseDTO;
import uz.script.wincrm.warehouse.response.WarehouseResponse;

@Component
public class WarehouseMapper {

    public Warehouse toEntity(WarehouseDTO dto) {
        return Warehouse.builder()
                .name(dto.getName())
                .status(Status.ACTIVE)
                .build();
    }

    public void updateEntity(Warehouse warehouse, WarehouseDTO dto) {
        warehouse.setName(dto.getName());
    }

    public WarehouseResponse toResponse(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .status(warehouse.getStatus())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .createdUsername(warehouse.getCreatedUsername())
                .build();
    }
}
