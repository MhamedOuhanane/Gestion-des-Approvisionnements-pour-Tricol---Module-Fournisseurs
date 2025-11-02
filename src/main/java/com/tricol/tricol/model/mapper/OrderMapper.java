package com.tricol.tricol.model.mapper;

import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductsOrderMapper.class})
public interface OrderMapper {
    @Mapping(source = "supplier.uuid", target = "supplierId")
    @Mapping(source = "supplier.contact", target = "supplierContact")
    OrderDTO toDto(Order order);

    @Mapping(target = "productsOrders", ignore = true)
    Order toEntity(OrderDTO dto);
}
