package uz.script.wincrm.suppliers.mapper;

import lombok.experimental.UtilityClass;
import uz.script.wincrm.suppliers.Supplier;
import uz.script.wincrm.suppliers.dto.SupplierDTO;
import uz.script.wincrm.suppliers.response.SupplierResponse;

@UtilityClass
public class SupplierMapper {

    /**
     * DTO -> Entity
     */
    public Supplier toEntity(SupplierDTO dto) {
        if (dto == null) {
            return null;
        }

        return Supplier.builder()
                .name(dto.getName())
                .inn(dto.getInn())
                .phone(dto.getPhone())
                .additionalPhone(dto.getAdditionalPhone())
                .address(dto.getAddress())
                .bankName(dto.getBankName())
                .mfo(dto.getMfo())
                .accountNumber(dto.getAccountNumber())
                .description(dto.getDescription())
                .build();
    }

    /**
     * Entity -> Response
     */
    public SupplierResponse toResponse(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .inn(supplier.getInn())
                .phone(supplier.getPhone())
                .additionalPhone(supplier.getAdditionalPhone())
                .address(supplier.getAddress())
                .bankName(supplier.getBankName())
                .mfo(supplier.getMfo())
                .accountNumber(supplier.getAccountNumber())
                .description(supplier.getDescription())
                .status(supplier.getStatus())
                .createdUsername(supplier.getCreatedUsername())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    /**
     * Update Entity
     */
    public void updateEntity(Supplier supplier, SupplierDTO dto) {
        supplier.setName(dto.getName());
        supplier.setInn(dto.getInn());
        supplier.setPhone(dto.getPhone());
        supplier.setAdditionalPhone(dto.getAdditionalPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setBankName(dto.getBankName());
        supplier.setMfo(dto.getMfo());
        supplier.setAccountNumber(dto.getAccountNumber());
        supplier.setDescription(dto.getDescription());
    }
}