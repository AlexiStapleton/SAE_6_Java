package com.usmb.but3.td4biblio.enumeration;

public enum TypeRecherche {

    CONTIENT("Contient"),
    COMMENCE_PAR("Commence par"),
    EGAL("Égal à");

    private final String label;

    TypeRecherche(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}