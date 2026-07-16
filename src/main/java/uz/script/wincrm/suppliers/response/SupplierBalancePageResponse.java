package uz.script.wincrm.suppliers.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import uz.script.wincrm.utils.response.PageResponse;

@Data
@Schema(name = "SupplierBalancePageResponse")
public class SupplierBalancePageResponse extends PageResponse<SupplierBalanceResponse> {
}