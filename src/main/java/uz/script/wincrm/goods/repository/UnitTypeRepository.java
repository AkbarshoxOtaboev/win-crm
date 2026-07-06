package uz.script.wincrm.goods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.goods.UnitType;

import java.util.Optional;

@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<UnitType> findByNameIgnoreCase(String name);
}
