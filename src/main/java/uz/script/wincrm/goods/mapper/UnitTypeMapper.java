package uz.script.wincrm.goods.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.goods.UnitType;
import uz.script.wincrm.goods.dto.UnitTypeDTO;
import uz.script.wincrm.goods.response.UnitTypeResponse;

@Component
public class UnitTypeMapper {

    /**
     * DTO -> Entity
     */
    public UnitType toEntity(UnitTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        UnitType unitType = new UnitType();
        unitType.setName(dto.getName());

        return unitType;
    }

    /**
     * DTO -> Entity (Update)
     */
    public void updateEntity(UnitType unitType, UnitTypeDTO dto) {
        if (unitType == null || dto == null) {
            return;
        }

        unitType.setName(dto.getName());
    }

    /**
     * Entity -> Response
     */
    public UnitTypeResponse toResponse(UnitType entity) {
        if (entity == null) {
            return null;
        }

        return UnitTypeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .createdUsername(entity.getCreatedUsername())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}