package com.tricol.tricol.model.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {
    private UUID uuid;
    private String company;
    private String address;
    private String contact;
    private String email;
    private String phone;
    private String city;
    private String ice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}