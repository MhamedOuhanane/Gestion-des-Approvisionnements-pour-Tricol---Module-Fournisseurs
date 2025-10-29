package com.tricol.tricol.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier extends Auditable {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String ice;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.DETACH)
    private List<Order> orders;
}