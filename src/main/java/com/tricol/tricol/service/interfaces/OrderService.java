package com.tricol.tricol.service.interfaces;

import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.SupplierDTO;

import java.util.Map;
import java.util.UUID;

public interface OrderService extends Service<OrderDTO> {
    Map<String, Object> findAllBySupplier(UUID supplierId, Map<String, Object> map);
    Map<String, Object> updateStatus(UUID uuid, String status);
}
