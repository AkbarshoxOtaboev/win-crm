package uz.script.wincrm.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.goods.Goods;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    boolean existsByBarcode(String barcode);
}