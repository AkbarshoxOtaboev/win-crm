package uz.script.wincrm.details.service;

import uz.script.wincrm.details.dto.CompanyDetailDTO;
import uz.script.wincrm.details.response.CompanyDetailResponse;

import java.util.List;

public interface CompanyDetailService {

    CompanyDetailResponse create(CompanyDetailDTO dto);

    CompanyDetailResponse findById(Long id);

    /**
     * Tizimdagi yagona (birinchi) kompaniya rekvizitini qaytaradi.
     */
    CompanyDetailResponse getCompanyDetail();

    List<CompanyDetailResponse> fetchAll();

    CompanyDetailResponse update(Long id, CompanyDetailDTO dto);

    void delete(Long id);
}