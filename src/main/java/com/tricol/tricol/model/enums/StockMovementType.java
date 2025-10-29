package com.tricol.tricol.model.enums;

public enum StockMovementType {
    PENDING("En attente"),
    VALIDATED("Validé"),
    DELIVERED("Livré"),
    CANCELED("Annulé");

    private final String desc;

    StockMovementType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
