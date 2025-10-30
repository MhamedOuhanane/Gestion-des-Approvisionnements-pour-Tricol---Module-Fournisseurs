package com.tricol.tricol.service.impl;

import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.mapper.SupplierMapper;
import com.tricol.tricol.repository.SupplierRepository;
import com.tricol.tricol.service.interfaces.Service;
import com.tricol.tricol.service.interfaces.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SupplierServiceImpl implements Service<SupplierDTO>, SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public Map<String, Object> create(SupplierDTO dto) {
        if (dto == null) {
            return Map.of(
                    "message", "Les informations de fournisseur ne peut pas étre vides",
                    "statusCode", 201,
                    "supplier", Optional.empty());
        }
        try {
            Supplier supplier = supplierRepository.save(supplierMapper.toEntity(dto));
            return Map.of("message", "Le fournisseur de mail '" + supplier.getEmail() + "' a été ajouté avec succès !",
                    "statusCode", 201,
                    "supplier", supplierMapper.toDto(supplier));
        } catch (RuntimeException e) {
            return Map.of(
                    "error", e.getMessage(),
                    "statusCode", 500,
                    "supplier", dto);
        }
    }

    @Override
    public Map<String, Object> findById(UUID uuid) {
        if (uuid == null) {
            return Map.of(
                    "message", "L'identifiant du fournisseur ne peut pas être vide",
                    "statusCode", 400,
                    "supplier", Optional.empty()
            );
        }
        try {
            Optional<Supplier> optionalSupplier = supplierRepository.findById(uuid);

            if (optionalSupplier.isEmpty()) {
                return Map.of(
                        "message", "Aucun fournisseur trouvé avec cet identifiant",
                        "statusCode", 404,
                        "supplier", Optional.empty()
                );
            }

            Supplier supplier = optionalSupplier.get();
            return Map.of(
                    "message", "Fournisseur trouvé avec succès",
                    "statusCode", 200,
                    "supplier", supplierMapper.toDto(supplier)
            );

        } catch (RuntimeException e) {
            return Map.of(
                    "error", e.getMessage(),
                    "statusCode", 500,
                    "supplier", Optional.empty()
            );
        }
    }

    @Override
    public Map<String, Object> findAll(Map<String, Object> map) {
        return Map.of();
    }

    @Override
    public Map<String, Object> update(UUID uuid, SupplierDTO dto) {
        return Map.of();
    }

    @Override
    public Map<String, Object> delete(UUID uuid) {
        return Map.of();
    }

    @Override
    public Map<String, Object> findByEmail(String email) {
        return Map.of();
    }

    @Override
    public Map<String, Object> findByIce(String ice) {
        return Map.of();
    }
}