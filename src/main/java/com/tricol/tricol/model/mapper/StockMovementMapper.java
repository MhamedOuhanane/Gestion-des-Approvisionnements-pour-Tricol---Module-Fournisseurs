package com.tricol.tricol.model.mapper;

import com.tricol.tricol.model.dto.StockMovementDTO;
import com.tricol.tricol.model.entity.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    @Mapping(source = "product.uuid", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    StockMovementDTO toDto(StockMovement stockMovement);

    StockMovement toEntity(StockMovementDTO dto);
}
