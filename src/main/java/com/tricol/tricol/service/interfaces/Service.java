package com.tricol.tricol.service.interfaces;

import java.util.Map;
import java.util.UUID;

public interface Service<D> {
    Map<String, Object> create(D dto);
    Map<String, Object> findById(UUID uuid);
    Map<String, Object> findAll(Map<String, Object> map);
    Map<String, Object> update(UUID uuid, D dto);
    Map<String, Object> delete(UUID uuid);
}
