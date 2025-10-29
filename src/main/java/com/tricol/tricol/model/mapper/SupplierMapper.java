package com.tricol.tricol.model.mapper;

import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDTO toDto(Supplier supplier);
    Supplier toEntity(SupplierDTO supplierDTO);
}