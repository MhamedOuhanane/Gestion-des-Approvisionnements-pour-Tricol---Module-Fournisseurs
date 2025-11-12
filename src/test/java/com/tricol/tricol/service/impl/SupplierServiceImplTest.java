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
    public  void createSupplier_shouldThrowException_whenEmailIsAlreadyExist() {
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

    @Test
    public void findSupplierById_shouldSucceed_whenValidData() {
        UUID uuid = UUID.randomUUID();
        Supplier supplier = new Supplier(
                uuid,
                "Zara",
                "Maroc",
                "Rabat",
                "Mohammed M",
                "mohammed@gmail.com",
                "+212783959384",
                "M98748593024895"
        );
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setUuid(supplier.getUuid());
        supplierDTO.setCompany(supplier.getCompany());
        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setCity(supplier.getCity());
        supplierDTO.setContact(supplier.getContact());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setPhone(supplier.getPhone());
        supplierDTO.setIce(supplier.getIce());

        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        Map<String, Object> result = supplierService.findById(uuid);

        assertEquals(supplierDTO, result.get("supplier"));
        assertEquals(200, result.get("status"));
        assertEquals("Fournisseur trouvé avec succès", result.get("message"));
    }

    @Test
    public void findSupplierById_shouldThrowException_whenUuidIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("L'identifiant du fournisseur ne peut pas être vide", exception.getMessage());
    }

    @Test
    public void findSupplierById_shouldThrowException_whenSupplierNotFound() {
        UUID uuid = UUID.randomUUID();

        when(supplierRepository.findById(uuid)).thenReturn(Optional.empty());
        AppException exception = assertThrows(AppException.class, () -> supplierService.findById(uuid));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aucun fournisseur trouvé avec cet identifiant", exception.getMessage());
    }

    @Test
    public void updateSupplier_shouldSucceed_whenValidUpdate() {
        UUID uuid = UUID.randomUUID();
        Supplier supplier = new Supplier(
                uuid,
                "Zara",
                "Maroc",
                "Rabat",
                "Mohammed M",
                "mohammed@gmail.com",
                "+212783959384",
                "M98748593024895"
        );
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompany("SE");
        supplierDTO.setAddress("France");
        supplierDTO.setCity("Paris");
        supplierDTO.setContact("Supplier");
        supplierDTO.setEmail("supplier@gmail.com");
        supplierDTO.setIce("784693075659048");

        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        when(supplierRepository.findByEmail(supplierDTO.getEmail())).thenReturn(Optional.empty());
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        supplierDTO.setUuid(uuid);
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        Map<String, Object> result = supplierService.update(uuid, supplierDTO);

        assertEquals(200, result.get("status"));
        assertEquals("Le fournisseur '" + supplier.getEmail() + "' a été mis à jour avec succès!", result.get("message"));
        assertEquals(supplierDTO, result.get("data"));
    }

    @Test
    public void updateSupplier_shouldSucceed_whenNoFieldsModified() {
        UUID uuid = UUID.randomUUID();
        Supplier supplier = new Supplier(
                uuid,
                "Zara",
                "Maroc",
                "Rabat",
                "Mohammed M",
                "mohammed@gmail.com",
                "+212783959384",
                "M98748593024895"
        );
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompany(supplier.getCompany());
        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setCity(supplier.getCity());
        supplierDTO.setContact(supplier.getContact());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setIce(supplier.getIce());

        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        supplierDTO.setUuid(uuid);
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        Map<String, Object> result = supplierService.update(uuid, supplierDTO);

        assertEquals(200, result.get("status"));
        assertEquals("Aucun champ du fournisseur n'a été modifié.", result.get("message"));
        assertEquals(supplierDTO, result.get("data"));
    }

    @Test
    public void updateSupplier_shouldThrowException_whenUuidIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.update(null, new SupplierDTO()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("L'identifiant du fournisseur ne peut pas être vide", exception.getMessage());
    }

    @Test
    public void updateSupplier_shouldThrowException_whenDtoIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.update(UUID.randomUUID(), null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Les informations de fournisseur ne peut pas étre vides", exception.getMessage());
    }

    @Test
    public void updateSupplier_shouldThrowException_whenSupplierNotFound() {
        UUID uuid = UUID.randomUUID();

        when(supplierRepository.findById(uuid)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> supplierService.update(uuid, new SupplierDTO()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aucun fournisseur trouvé avec cet identifiant", exception.getMessage());
    }

    @Test
    public void updateSupplier_shouldThrowException_whenUpdateEmailIsAlreadyExist() {
        UUID uuid = UUID.randomUUID();
        Supplier supplier = new Supplier(
                uuid,
                "Zara",
                "Maroc",
                "Rabat",
                "Mohammed M",
                "mohammed@gmail.com",
                "+212783959384",
                "M98748593024895"
        );
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setEmail("supplier@gmail.com");

        when(supplierRepository.findById(uuid)).thenReturn(Optional.of(supplier));
        when(supplierRepository.findByEmail(supplierDTO.getEmail())).thenReturn(Optional.of(new Supplier()));

        AppException exception = assertThrows(AppException.class, () -> supplierService.update(uuid, supplierDTO));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Un fournisseur avec l'email '" + supplierDTO.getEmail() + "' existe déjà.", exception.getMessage());
    }

    @Test
    public void deleteSupplier_shouldSucceed_whenSupplierDeletedSuccessfully() {
        UUID uuid = UUID.randomUUID();
        Supplier supplier = new Supplier();
        supplier.setUuid(uuid);

        when(supplierRepository.findById(uuid))
                .thenReturn(Optional.of(supplier))
                .thenReturn(Optional.empty());

        Map<String, Object> result = supplierService.delete(uuid);

        assertEquals(200, result.get("status"));
        assertEquals("Le fournisseur a été supprimé avec succès.", result.get("message"));
        assertEquals(Optional.empty(), result.get("data"));
    }

    @Test
    public void deleteSupplier_shouldFail_whenDeletingSupplierFails() {
        UUID uuid = UUID.randomUUID();
        Supplier supplier = new Supplier();
        supplier.setUuid(uuid);
        SupplierDTO dto = new SupplierDTO();
        dto.setUuid(uuid);

        when(supplierRepository.findById(uuid))
                .thenReturn(Optional.of(supplier))
                .thenReturn(Optional.of(supplier));
        when(supplierMapper.toDto(supplier)).thenReturn(dto);

        Map<String, Object> result = supplierService.delete(uuid);

        assertEquals(500, result.get("status"));
        assertEquals("Échec de la suppression du fournisseur.", result.get("message"));
        assertEquals(dto, result.get("data"));
    }

    @Test
    public void deleteSupplier_shouldThrowException_whenUuidIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.delete(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("L'identifiant du fournisseur ne peut pas être vide", exception.getMessage());
    }

    @Test
    public void deleteSupplier_shouldThrowException_whenSupplierNotFound() {
        UUID uuid = UUID.randomUUID();

        when(supplierRepository.findById(uuid)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> supplierService.delete(uuid));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aucun fournisseur trouvé avec cet identifiant", exception.getMessage());
    }

    @Test
    public void findSupplierByEmail_shouldSucceed_whenSupplierFound() {
        String email = "mhmde@gmail.com";
        Supplier supplier = new Supplier();
        SupplierDTO dto = new SupplierDTO();

        when(supplierRepository.findByEmail(email)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDto(supplier)).thenReturn(dto);

        Map<String, Object> result = supplierService.findByEmail(email);

        assertEquals(200, result.get("status"));
        assertEquals("Fournisseur trouvé avec succès", result.get("message"));
        assertEquals(dto, result.get("supplier"));
    }

    @Test
    public void findSupplierByEmail_shouldThrowException_whenEmailIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.findByEmail(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("L'address mail du fournisseur ne peut pas être vide", exception.getMessage());
    }

    @Test
    public void findSupplierByEmail_shouldThrowException_whenSupplierNotFound() {
        String email = "mhamed@gmail.com";

        when(supplierRepository.findByEmail(email)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> supplierService.findByEmail(email));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aucun fournisseur trouvé avec cet email", exception.getMessage());
    }

    @Test
    public void findSupplierByIce_shouldSucceed_whenSupplierFound() {
        String ice = "mhmde@gmail.com";
        Supplier supplier = new Supplier();
        SupplierDTO dto = new SupplierDTO();

        when(supplierRepository.findByIce(ice)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDto(supplier)).thenReturn(dto);

        Map<String, Object> result = supplierService.findByIce(ice);

        assertEquals(200, result.get("status"));
        assertEquals("Fournisseur trouvé avec succès", result.get("message"));
        assertEquals(dto, result.get("supplier"));
    }

    @Test
    public void findSupplierByIce_shouldThrowException_whenIceIsNull() {
        AppException exception = assertThrows(AppException.class, () -> supplierService.findByIce(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("L'ICE du fournisseur ne peut pas être vide", exception.getMessage());
    }

    @Test
    public void findSupplierByIce_shouldThrowException_whenSupplierNotFound() {
        String ice = "mhamed@gmail.com";

        when(supplierRepository.findByIce(ice)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> supplierService.findByIce(ice));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aucun fournisseur trouvé avec cet code ice", exception.getMessage());
    }

}
