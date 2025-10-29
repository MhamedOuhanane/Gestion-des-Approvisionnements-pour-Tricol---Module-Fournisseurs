package com.tricol.tricol.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
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

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}