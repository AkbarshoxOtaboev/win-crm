package uz.script.wincrm.sale.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import uz.script.wincrm.utils.response.PageResponse;
@Data
@Schema(name = "SaleOrderItemPageResponse")
public class SaleOrderItemPageResponse extends PageResponse<SaleOrderItemResponse> {
}
