package uz.script.wincrm.goods.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.goods.GoodsGroup;
import uz.script.wincrm.goods.dto.GoodsGroupDTO;
import uz.script.wincrm.goods.response.GoodsGroupResponse;

@Component
public class GoodsGroupMapper {

    /**
     * DTO -> Entity
     */
    public GoodsGroup toEntity(GoodsGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        GoodsGroup entity = new GoodsGroup();
        entity.setName(dto.getName());

        return entity;
    }

    /**
     * DTO -> Entity (Update)
     */
    public void updateEntity(GoodsGroup entity, GoodsGroupDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.getName());
    }

    /**
     * Entity -> Response
     */
    public GoodsGroupResponse toResponse(GoodsGroup entity) {
        if (entity == null) {
            return null;
        }

        return GoodsGroupResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .createdUsername(entity.getCreatedUsername())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}