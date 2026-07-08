package uz.script.wincrm.exceptions;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {
    private Long goodsId;
    private Long warehouseId;
    private java.math.BigDecimal available;
    private java.math.BigDecimal requested;

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Long goodsId, Long warehouseId,
                                      java.math.BigDecimal available, java.math.BigDecimal requested) {
        super(message);
        this.goodsId = goodsId;
        this.warehouseId = warehouseId;
        this.available = available;
        this.requested = requested;
    }

}