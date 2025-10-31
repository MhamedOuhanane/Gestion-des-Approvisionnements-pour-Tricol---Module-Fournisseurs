package com.tricol.tricol.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierDTO {
    private UUID uuid;

    @NotBlank(message = "la societe est obligatoire")
    private String company;

    @NotBlank(message = "l'adresse est obligatoire")
    private String address;

    @NotBlank(message = "le contact est obligatoire")
    private String contact;

    @NotBlank(message = "Adresse e-mail est obligatoire")
    @Email(message = "Adresse e-mail invalide")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est Oblegatoire")
    @Pattern(regexp = "^\\+212\\d{9}$", message = "Le numéro de téléphone doit commencer par +212 et contenir exactement 9 chiffres après")
    private String phone;

    @NotBlank(message = "la ville est obligatoire")
    private String city;

    @NotBlank(message = "le code ICE est obligatoire")
    @Pattern(regexp ="\\d{15}", message = "Le code ICE doit contenir exactement 15 chiffres")
    private String ice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SupplierDTO() {}

    public SupplierDTO(UUID uuid, String company, String address, String contact, String email, String phone, String city, String ice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.company = company;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.ice = ice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}