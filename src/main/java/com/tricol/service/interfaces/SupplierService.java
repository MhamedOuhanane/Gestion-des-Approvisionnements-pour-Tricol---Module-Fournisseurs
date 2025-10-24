package com.tricol.service.interfaces;

import com.tricol.model.dto.SupplierDTO;
import java.util.Map;
import java.util.UUID;

public interface SupplierService {
    Map<String, Object> addSupplier(SupplierDTO dto);
    Map<String, Object> findSupplier(UUID uuid);
    Map<String, Object> updateSupplier(UUID uuid, SupplierDTO dto);
    Map<String, Object> getAllSupplier();
    Map<String, Object> deleteSupplier(UUID uuid);
}