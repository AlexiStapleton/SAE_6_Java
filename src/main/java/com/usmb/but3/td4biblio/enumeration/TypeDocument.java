package com.usmb.but3.td4biblio.enumeration;

public enum TypeDocument {

    LIVRE("Livre"),
    DVD("DVD");

    private final String label;

    TypeDocument(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
