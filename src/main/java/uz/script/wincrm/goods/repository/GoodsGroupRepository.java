package uz.script.wincrm.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.goods.GoodsGroup;

import java.util.Optional;

@Repository
public interface GoodsGroupRepository extends JpaRepository<GoodsGroup, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<GoodsGroup> findByNameIgnoreCase(String name);
}
