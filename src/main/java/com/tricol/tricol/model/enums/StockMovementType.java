package com.tricol.tricol.model.enums;

public enum StockMovementType {
    ENTREE("Entrée"),
    SORTIE("Sortie"),
    ADJUSTMENT("Ajustement");

    private final String desc;

    StockMovementType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

