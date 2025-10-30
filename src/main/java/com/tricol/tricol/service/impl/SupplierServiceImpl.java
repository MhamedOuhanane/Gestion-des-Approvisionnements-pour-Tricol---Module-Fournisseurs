package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.mapper.SupplierMapper;
import com.tricol.tricol.repository.SupplierRepository;
import com.tricol.tricol.service.interfaces.Service;
import com.tricol.tricol.service.interfaces.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.List;
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
        if (dto == null) throw new AppException("Les informations de fournisseur ne peut pas étre vides", HttpStatus.BAD_REQUEST);
        if (supplierRepository.findByEmail(dto.getEmail()).isPresent())  {
            throw new AppException(
                    "Un fournisseur avec l'email '" + dto.getEmail() + "' existe déjà.",
                    HttpStatus.CONFLICT
            );
        }
        Supplier supplier = supplierRepository.save(supplierMapper.toEntity(dto));
        return Map.of("message", "Le fournisseur de mail '" + supplier.getEmail() + "' a été ajouté avec succès !",
                "status", 201,
                "supplier", supplierMapper.toDto(supplier));
    }

    @Override
    public Map<String, Object> findById(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du fournisseur ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Supplier supplier = supplierRepository.findById(uuid)
                .orElseThrow(() -> new AppException("Aucun fournisseur trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        return Map.of(
                "message", "Fournisseur trouvé avec succès",
                "status", 200,
                "supplier", supplierMapper.toDto(supplier)
        );
    }

    @Override
    public Map<String, Object> findAll(Map<String, Object> map) {
        if (map.isEmpty()) {
            return Map.of("page", 0, "size", 5);
        }
        Pageable pageable = PageRequest.of((int) map.get("page"), (int) map.get("size"), Sort.by("contact").ascending());
        String city = (String) map.getOrDefault("city", "");
        String company = (String) map.getOrDefault("company", "");
        String contact = (String) map.getOrDefault("contact", "");
        Page<Supplier> suppliers = suppliers = supplierRepository.findByCityContainingAndContactContainingAndCompanyContaining(city, contact, company, pageable);

        String message;
        Object data;
        if (suppliers.isEmpty()) {
            message = "Aucun fournisseur n'existe dans le système";
            data = suppliers.stream()
                    .map(supplierMapper::toDto)
                    .toList();
        } else {
            message = "Tous les fournisseurs trouvés avec succès";
            data = List.of();
        }

        return Map.of(
                "message", message,
                "status", 200,
                "data", data
        );
    }

    @Override
    public Map<String, Object> update(UUID uuid, SupplierDTO dto) {
        if (uuid == null) throw new AppException("L'identifiant du fournisseur ne peut pas être vide", HttpStatus.BAD_REQUEST);
        if (dto == null) throw new AppException("Les informations de fournisseur ne peut pas étre vides", HttpStatus.BAD_REQUEST);
        Supplier supplier = supplierRepository.findById(uuid)
                .orElseThrow(() -> new AppException("Aucun fournisseur trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        if (!supplier.getEmail().equals(dto.getEmail()) && supplierRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new AppException(
                    "Un fournisseur avec l'email '" + dto.getEmail() + "' existe déjà.",
                    HttpStatus.CONFLICT,
                    supplier
            );
        }
        supplier.setCompany(dto.getCompany());
        supplier.setContact(dto.getContact());
        supplier.setEmail(dto.getEmail());
        supplier.setIce(dto.getIce());
        supplier.setCity(dto.getCity());
        supplier.setAddress(dto.getAddress());
        supplier = supplierRepository.save(supplierMapper.toEntity(dto));
        return Map.of(
                "message", "Le fournisseur '" + supplier.getEmail() + "' a été mis à jour avec succès !",
                "status", 200,
                "data", supplierMapper.toDto(supplier)
        );
    }

    @Override
    public Map<String, Object> delete(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du fournisseur ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Supplier supplier = supplierRepository.findById(uuid)
                .orElseThrow(() -> new AppException("Aucun fournisseur trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        supplierRepository.delete(supplier);
        boolean deleted = supplierRepository.findById(uuid).isPresent();
        String message = deleted
                ? "Le fournisseur a été supprimé avec succès."
                : "Échec de la suppression du fournisseur.";
        int status = deleted ? 200 : 500;
        return Map.of(
                "message", message,
                "status", status,
                "data", deleted ? Optional.empty() : supplierMapper.toDto(supplier)
        );
    }

    @Override
    public Map<String, Object> findByEmail(String email) {
        if (email == null) throw new AppException("L'address mail du fournisseur ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Supplier supplier = supplierRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Aucun fournisseur trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        return Map.of(
                "message", "Fournisseur trouvé avec succès",
                "status", 200,
                "supplier", supplierMapper.toDto(supplier)
        );
    }

    @Override
    public Map<String, Object> findByIce(String ice) {
        if (ice == null) throw new AppException("L'ICE du fournisseur ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Supplier supplier = supplierRepository.findByIce(ice)
                .orElseThrow(() -> new AppException("Aucun fournisseur trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        return Map.of(
                "message", "Fournisseur trouvé avec succès",
                "status", 200,
                "supplier", supplierMapper.toDto(supplier)
        );
    }
}