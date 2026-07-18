package uz.script.wincrm.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.stock.StockTransfer;

import java.util.List;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    List<StockTransfer> findAllByGoodsId(Long goodsId);

    // Ombor yoki manba, yoki maqsad sifatida ishtirok etgan barcha transferlarni qaytaradi
    List<StockTransfer> findAllByFromWarehouseIdOrToWarehouseId(Long fromWarehouseId, Long toWarehouseId);
}
