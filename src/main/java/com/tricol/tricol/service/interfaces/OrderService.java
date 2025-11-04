package com.tricol.tricol.service.interfaces;

import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.enums.OrderStatus;

import java.util.Map;
import java.util.UUID;

public interface OrderService extends Service<OrderDTO> {
    Map<String, Object> findAllBySupplier(UUID supplierId, Map<String, Object> map);
    Map<String, Object> updateStatus(UUID uuid, OrderStatus status);
}
