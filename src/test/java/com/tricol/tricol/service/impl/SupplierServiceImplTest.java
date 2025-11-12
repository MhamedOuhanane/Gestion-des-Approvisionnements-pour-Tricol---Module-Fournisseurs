package com.tricol.tricol.service.impl;

import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.mapper.SupplierMapper;
import com.tricol.tricol.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {
    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;


}
