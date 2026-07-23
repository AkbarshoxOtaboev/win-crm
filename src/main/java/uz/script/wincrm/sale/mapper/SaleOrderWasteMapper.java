package uz.script.wincrm.sale.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.sale.SaleOrderWaste;
import uz.script.wincrm.sale.dto.SaleOrderWasteDTO;
import uz.script.wincrm.sale.response.SaleOrderWasteResponse;

@Component
public class SaleOrderWasteMapper {

    public SaleOrderWaste toEntity(SaleOrderWasteDTO dto) {
        if (dto == null) {
            return null;
        }
        // DIQQAT: saleOrder, goods, goodsName, unitName bu yerda O'RNATILMAYDI - ular servisda
        // topilgan Goods'dan olib, snapshot sifatida to'ldiriladi (SaleOrderMapper'dagi
        // totalSum bilan bir xil sabab: hisob-kitob/snapshot mas'uliyati servis qatlamida bo'lishi kerak).
        return SaleOrderWaste.builder()
                .quantity(dto.getQuantity())
                .width(dto.getWidth())
                .height(dto.getHeight())
                .comment(dto.getComment())
                .build();
    }

    public void updateEntity(SaleOrderWaste entity, SaleOrderWasteDTO dto) {
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getWidth() != null) {
            entity.setWidth(dto.getWidth());
        }
        if (dto.getHeight() != null) {
            entity.setHeight(dto.getHeight());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
        // saleOrderId va goodsId update paytida qasddan o'zgartirilmaydi - yozuv qaysi
        // buyurtma/materialga tegishli ekanligi yaratilgandan keyin o'zgarmas bo'lishi kerak.
    }

    public SaleOrderWasteResponse toResponse(SaleOrderWaste entity) {
        if (entity == null) {
            return null;
        }
        return SaleOrderWasteResponse.builder()
                .id(entity.getId())
                .saleOrderId(entity.getSaleOrder() != null ? entity.getSaleOrder().getId() : null)
                .goodsId(entity.getGoods() != null ? entity.getGoods().getId() : null)
                .goodsName(entity.getGoodsName())
                .unitName(entity.getUnitName())
                .quantity(entity.getQuantity())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .comment(entity.getComment())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedUserId())
                .build();
    }
}