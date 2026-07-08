package uz.script.wincrm.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.goods.Goods;

import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    boolean existsByBarcode(String barcode);


    Optional<Goods> findByBarcode(String barcode);

    boolean existsByName(String name);

    @Query("SELECT g FROM Goods g WHERE g.id = :id AND g.status <> 'DELETED'")
    Optional<Goods> findByIdActive(@Param("id") Long id);
}