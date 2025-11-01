package com.tricol.tricol.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="suppliers")
public class Supplier extends Auditable {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @NotBlank(message = "la societe est obligatoire")
    @Column(nullable = false)
    private String company;

    @NotBlank(message = "l'adresse est obligatoire")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "le contact est obligatoire")
    @Column(nullable = false)
    private String contact;

    @NotBlank(message = "Adresse e-mail est obligatoire")
    @Email(message = "Adresse e-mail invalide")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le numéro de téléphone est Oblegatoire")
    @Pattern(regexp = "^\\+212\\d{9}$", message = "Le numéro de téléphone doit commencer par +212 et contenir exactement 9 chiffres après")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "la ville est obligatoire")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "le code ICE est obligatoire")
    @Pattern(regexp ="\\d{15}", message = "Le code ICE doit contenir exactement 15 chiffres")
    @Column(nullable = false)
    private String ice;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.DETACH)
    private List<Order> orders;

    public Supplier() {}

    public Supplier(UUID uuid, String company, String address, String contact, String email, String phone, String city, String ice) {
        this.uuid = uuid;
        this.company = company;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.ice = ice;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}