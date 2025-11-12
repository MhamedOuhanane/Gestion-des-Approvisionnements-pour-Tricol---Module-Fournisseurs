package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.mapper.SupplierMapper;
import com.tricol.tricol.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {
    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    public void createSupplier_shouldSucceed_whenValidData() {
        UUID uuid = UUID.randomUUID();
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompany("Zara");
        supplierDTO.setAddress("Maroc");
        supplierDTO.setCity("Rabat");
        supplierDTO.setContact("Mohammed M");
        supplierDTO.setEmail("mohammed@gmail.com");
        supplierDTO.setPhone("+212783959384");
        supplierDTO.setIce("M98748593024895");
        Supplier supplier = new Supplier(
                uuid,
                supplierDTO.getCompany(),
                supplierDTO.getAddress(),
                supplierDTO.getContact(),
                supplierDTO.getEmail(),
                supplierDTO.getPhone(),
                supplierDTO.getCity(),
                supplierDTO.getIce()
        );

        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.findByEmail(supplierDTO.getEmail())).thenReturn(Optional.empty());
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        supplierDTO.setUuid(uuid);
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        Map<String, Object> result = supplierService.create(supplierDTO);

        assertEquals(supplierDTO, (SupplierDTO) result.get("supplier"));
        assertEquals(201, result.get("status"));
        assertEquals("Le fournisseur de mail '" + supplierDTO.getEmail() + "' a été ajouté avec succès !", result.get("message"));
    }

    @Test
    public void createSupplier_shouldThrowException_whenDtoIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.create(null));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Les informations de fournisseur ne peut pas étre vides", exception.getMessage());
    }

    @Test
    public  void createSupplier_shouldThrowException_whenEmailAlreadyExist() {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompany("Zara");
        supplierDTO.setAddress("Maroc");
        supplierDTO.setCity("Rabat");
        supplierDTO.setContact("Mohammed M");
        supplierDTO.setEmail("mohammed@gmail.com");
        supplierDTO.setPhone("+212783959384");
        supplierDTO.setIce("M98748593024895");

        when(supplierRepository.findByEmail(supplierDTO.getEmail())).thenReturn(Optional.of(new Supplier()));

        AppException exception = assertThrows(AppException.class, () -> supplierService.create(supplierDTO));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Un fournisseur avec l'email '" + supplierDTO.getEmail() + "' existe déjà.", exception.getMessage());
    }
}
