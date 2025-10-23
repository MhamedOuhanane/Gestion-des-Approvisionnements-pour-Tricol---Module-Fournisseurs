package com.tricol.model.mapper;

import com.tricol.model.dto.SupplierDTO;
import com.tricol.model.entity.Supplier;

public class SupplierMapper {
    public static SupplierDTO toDTO(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        SupplierDTO dto = new SupplierDTO();
        dto.setUuid(supplier.getUuid());
        dto.setCompany(supplier.getCompany());
        dto.setAddress(supplier.getAddress());
        dto.setContact(supplier.getContact());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setCity(supplier.getCity());
        dto.setICE(supplier.getICE());

        return dto;
    }

    public static Supplier toEntity(SupplierDTO dto) {
        if (dto == null) {return null;}
        Supplier supplier = new Supplier();

        supplier.setUuid(dto.getUuid());
        supplier.setCompany(dto.getCompany());
        supplier.setAddress(dto.getAddress());
        supplier.setContact(dto.getContact());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setCity(dto.getCity());
        supplier.setICE(dto.getICE());

        return supplier;
    }
}
