package uz.script.wincrm.details.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.details.CompanyDetail;

import java.util.Optional;

@Repository
public interface CompanyDetailRepository extends JpaRepository<CompanyDetail, Long> {

    Optional<CompanyDetail> findByInn(String inn);

    /**
     * CompanyDetail odatda tizimda bitta yozuv sifatida saqlanadi
     * (kompaniya rekvizitlari). Shu birinchi (eng eski) yozuvni qaytaradi.
     */
    Optional<CompanyDetail> findFirstByOrderByIdAsc();
}