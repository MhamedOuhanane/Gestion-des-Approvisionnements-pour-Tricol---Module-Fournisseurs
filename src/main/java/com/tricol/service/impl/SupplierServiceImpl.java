package com.tricol.service.impl;

import com.tricol.model.dto.SupplierDTO;
import com.tricol.model.entity.Supplier;
import com.tricol.model.mapper.SupplierMapper;
import com.tricol.repository.SupplierRepository;
import com.tricol.service.interfaces.SupplierService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Map<String, Object> addSupplier(SupplierDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Les informations de fournisseur ne peut pas étre vides");
        try {
            Supplier supplier = supplierRepository.save(SupplierMapper.toEntity(dto));
            return Map.of("message", "Le fournisseur de mail \"" + supplier.getEmail() + "\" a été ajouté avec succès !", "supplier", SupplierMapper.toDTO(supplier));
        } catch (RuntimeException e) {
            return Map.of("error", e.getMessage(), "supplier", dto);
        }
    }

    @Override
    public Map<String, Object> findSupplier(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("L'identifiant de fournisseur ne peut pas étre vide");
        try {
            Supplier supplier = supplierRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Ce fournisseur n'existe pas"));
            return Map.of("message", "Fournisseur trouvée avec succès!", "supplier", SupplierMapper.toDTO(supplier));
        } catch (RuntimeException e) {
            return Map.of("error", e.getMessage(), "supplier", null);
        }
    }

    @Override
    public Map<String, Object> updateSupplier(UUID uuid, SupplierDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Les informations de fournisseur ne peut pas étre vides");
        if (uuid == null) throw new IllegalArgumentException("L'identifiant de fournisseur ne peut pas étre vide");
        try {
            Map<String, Object> supplierMap = findSupplier(uuid);
            Supplier supplier = (Supplier) supplierMap.get("supplier");

            boolean updated = false;
            if (dto.getCompany() != null) {
                supplier.setCompany(dto.getCompany());
                updated = true;
            }
            if (dto.getAddress() != null) {
                supplier.setAddress(dto.getAddress());
                updated = true;
            }
            if (dto.getContact() != null) {
                supplier.setContact(dto.getContact());
                updated = true;
            }
            if (dto.getEmail() != null) {
                supplier.setEmail(dto.getEmail());
                updated = true;
            }
            if (dto.getPhone() != null) {
                supplier.setPhone(dto.getPhone());
                updated = true;
            }
            if (dto.getCity() != null) {
                supplier.setCity(dto.getCity());
                updated = true;
            }
            if (dto.getICE() != null) {
                supplier.setICE(dto.getICE());
                updated = true;
            }

            if (!updated) return Map.of("warning", "Il n'y a aucune modification à apporter aux informations du fournisseur.", "supplier", supplier);
            supplier = supplierRepository.save(supplier);
            return Map.of("message", "Les information du fournisseur de mail \"" + supplier.getEmail() + "\" est modifié avec succès!", "supplier", SupplierMapper.toDTO(supplier));
        } catch (RuntimeException e) {
            return Map.of("error", e.getMessage(), "supplier", null);
        }
    }

    @Override
    public Map<String, Object> getAllSupplier() {
        try {
            List<Supplier> list = supplierRepository.findAll();
            String message = list.isEmpty() ? "Aucun fournisseur n'existe dans le système" : "Trouver tous les fournisseur exist avec succès!";
            List<SupplierDTO> dtos = list.stream().map(SupplierMapper::toDTO).toList();
            return Map.of("messge", message, "suppliers", dtos);
        } catch (RuntimeException e) {
            return Map.of("error", e.getMessage(), "suppliers", List.of());
        }
    }

    @Override
    public Map<String, Object> deleteSupplier(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("L'identifiant de fournisseur ne peut pas étre vide");
        try {
            Map<String, Object> supplierMap = findSupplier(uuid);
            Supplier supplier = (Supplier) supplierMap.get("supplier");
            supplierRepository.delete(supplier);
            return Map.of("message", "Le fournisseur de mail \"" + supplier.getEmail() + "\" est supprimè avec succès!", "supplier", SupplierMapper.toDTO(supplier));
        } catch (RuntimeException e) {
            return Map.of("error", e.getMessage(), "supplier", null);
        }
    }
}
