package uz.script.wincrm.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.script.wincrm.sale.enums.SalesOrderStatus;

/**
 * DIQQAT: Bu DTO uchun alohida public "create" endpoint YO'Q va bo'lmasligi kerak -
 * aks holda istalgan foydalanuvchi soxta tarix yozuvlari yaratishi mumkin bo'lardi.
 * Faqat SaleOrderServiceImpl.create() va changeStatus() ichidan,
 * SaleOrderHistoryService.recordHistory(dto) orqali chaqiriladi.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SaleOrder History DTO", description = "Internal DTO used to record a sale order status change")
public class SaleOrderHistoryDTO {

    @NotNull(message = "Sale order ID is required")
    @Schema(description = "Sale order ID", example = "1")
    private Long saleOrderId;

    @Schema(description = "Previous status (null when the order is being created)", example = "NEW")
    private SalesOrderStatus fromStatus;

    @NotNull(message = "Target status is required")
    @Schema(description = "New status", example = "CONFIRMED")
    private SalesOrderStatus toStatus;

    @Schema(description = "Optional comment about why the status changed", example = "Mijoz telefon orqali tasdiqladi")
    private String comment;
}