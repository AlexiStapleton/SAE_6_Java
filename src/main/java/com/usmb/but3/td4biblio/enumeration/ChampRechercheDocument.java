package com.usmb.but3.td4biblio.enumeration;

public enum ChampRechercheDocument {

    TITRE("Titre"),
    AUTEUR("Auteur"),
    EDITEUR("Éditeur"),
    BIBLIOTHEQUE("Bibliothèque"),
    GENRE("Genre");

    private final String label;

    ChampRechercheDocument(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}