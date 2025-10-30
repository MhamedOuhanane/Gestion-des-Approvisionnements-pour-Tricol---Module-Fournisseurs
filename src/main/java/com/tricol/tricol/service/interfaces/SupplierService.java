package com.tricol.tricol.service.interfaces;

import java.util.Map;

public interface SupplierService {
    Map<String, Object> findByEmail(String email);
    Map<String, Object> findByIce(String ice);
}