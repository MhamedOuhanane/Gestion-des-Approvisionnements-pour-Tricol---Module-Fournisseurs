package com.tricol.tricol.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="suppliers")
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

//    @OneToMany(mappedBy = "orders", cascade = CascadeType.DETACH)
//    private List<Order> orders;

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
}