package com.tricol.tricol.service.interfaces;

import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.SupplierDTO;

import java.util.Map;

public interface OrderService extends Service<OrderDTO> {
    Map<String, Object> findAllBySupplier(SupplierDTO dto);
}
