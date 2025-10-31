package com.tricol.tricol.service.interfaces;

import com.tricol.tricol.model.dto.SupplierDTO;

import java.util.Map;

public interface SupplierService extends Service<SupplierDTO>{
    Map<String, Object> findByEmail(String email);
    Map<String, Object> findByIce(String ice);
}