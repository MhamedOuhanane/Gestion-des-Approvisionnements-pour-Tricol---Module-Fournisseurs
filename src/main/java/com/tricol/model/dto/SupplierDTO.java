package com.tricol.model.dto;

import java.util.UUID;

public class SupplierDTO {
    private UUID uuid;
    private String company;
    private String address;
    private String contact;
    private String email;
    private String phone;
    private String city;
    private Long ICE;

    public SupplierDTO() {}

    public SupplierDTO(UUID uuid, String company, String address, String contact, String email, String phone, String city, Long ICE) {
        this.uuid = uuid;
        this.company = company;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.ICE = ICE;
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

    public Long getICE() {
        return ICE;
    }

    public void setICE(Long ICE) {
        this.ICE = ICE;
    }
}
