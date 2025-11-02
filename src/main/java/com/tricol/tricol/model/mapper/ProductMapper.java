package com.tricol.tricol.model.mapper;

import com.tricol.tricol.model.dto.ProductDTO;
import com.tricol.tricol.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO dto);
}
