package uz.script.wincrm.details.mapper;

import org.springframework.stereotype.Component;
import uz.script.wincrm.details.CompanyDetail;
import uz.script.wincrm.details.dto.CompanyDetailDTO;
import uz.script.wincrm.details.response.CompanyDetailResponse;

@Component
public class CompanyDetailMapper {

    public CompanyDetail toEntity(CompanyDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        return CompanyDetail.builder()
                .companyName(dto.getCompanyName())
                .inn(dto.getInn())
                .oked(dto.getOked())
                .mfo(dto.getMfo())
                .accountNumber(dto.getAccountNumber())
                .bankName(dto.getBankName())
                .director(dto.getDirector())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .build();
    }

    public void updateEntity(CompanyDetail entity, CompanyDetailDTO dto) {
        if (dto.getCompanyName() != null) {
            entity.setCompanyName(dto.getCompanyName());
        }
        if (dto.getInn() != null) {
            entity.setInn(dto.getInn());
        }
        if (dto.getOked() != null) {
            entity.setOked(dto.getOked());
        }
        if (dto.getMfo() != null) {
            entity.setMfo(dto.getMfo());
        }
        if (dto.getAccountNumber() != null) {
            entity.setAccountNumber(dto.getAccountNumber());
        }
        if (dto.getBankName() != null) {
            entity.setBankName(dto.getBankName());
        }
        if (dto.getDirector() != null) {
            entity.setDirector(dto.getDirector());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }

    public CompanyDetailResponse toResponse(CompanyDetail entity) {
        if (entity == null) {
            return null;
        }
        return CompanyDetailResponse.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .inn(entity.getInn())
                .oked(entity.getOked())
                .mfo(entity.getMfo())
                .accountNumber(entity.getAccountNumber())
                .bankName(entity.getBankName())
                .director(entity.getDirector())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedUserId())
                .build();
    }
}