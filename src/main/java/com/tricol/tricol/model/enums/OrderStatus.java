package com.tricol.tricol.model.enums;

public enum OrderStatus {
    PENDING("En attente"),
    VALIDATED("Validé"),
    DELIVERED("Livré"),
    CANCELED("Annulé");

    private final String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
