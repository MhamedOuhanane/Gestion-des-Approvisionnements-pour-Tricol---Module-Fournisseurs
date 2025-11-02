package com.tricol.tricol.model.mapper;

import com.tricol.tricol.model.dto.ProductsOrderDTO;
import com.tricol.tricol.model.entity.ProductsOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductsOrderMapper {
    @Mapping(source = "product.uuid", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "order.uuid", target = "orderId")
    ProductsOrderDTO toDto(ProductsOrder productsOrder);

    ProductsOrder toEntity(ProductsOrderDTO dto);
}
