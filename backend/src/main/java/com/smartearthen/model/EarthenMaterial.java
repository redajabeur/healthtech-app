package com.smartearthen.model;

public enum EarthenMaterial {
    PISE("Pisé compacté"),
    ADOBE("Adobe — Briques crues"),
    BTC("Briques de Terre Comprimée"),
    COB("Cob — Terre fibré"),
    TORCHIS("Torchis"),
    BAUGE("Bauge");

    private final String label;
    EarthenMaterial(String label) { this.label = label; }
    public String getLabel() { return label; }
}
