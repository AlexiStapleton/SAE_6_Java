package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

/**
 * Une classe entité qui représente une table de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "auteur", schema = "biblio")
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String nom;
    private String prenom;
    private String nationalite;
    private LocalDate dateNaissance;
    private LocalDate dateDeces;

    @ManyToMany
    @JoinTable(
            name = "auteur_type_auteur",
            schema = "biblio",
            joinColumns = @JoinColumn(name = "auteur_id"),
            inverseJoinColumns = @JoinColumn(name = "type_auteur_id")
    )
    private List<TypeAuteur> typesAuteur;

    @OneToMany
    private List<Livre> livres;

    public boolean isEqualTo(Auteur auteur) {
        if (this == auteur) return true;
        if (auteur == null) return false;
        if (Id != null ? !Id.equals(auteur.Id) : auteur.Id != null) return false;
        if (nom != null ? !nom.equals(auteur.nom) : auteur.nom != null) return false;
        if (prenom != null ? !prenom.equals(auteur.prenom) : auteur.prenom != null) return false;
        if (nationalite != null ? !nationalite.equals(auteur.nationalite) : auteur.nationalite != null) return false;
        if (dateNaissance != null ? !dateNaissance.equals(auteur.dateNaissance) : auteur.dateNaissance != null) return false;
        return dateDeces != null ? dateDeces.equals(auteur.dateDeces) : auteur.dateDeces == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isEqualTo((Auteur) obj);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (Id != null ? Id.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (nationalite != null ? nationalite.hashCode() : 0);
        result = 31 * result + (dateNaissance != null ? dateNaissance.hashCode() : 0);
        result = 31 * result + (dateDeces != null ? dateDeces.hashCode() : 0);
        return result;
    }

    public String getDesc() {
        return prenom + " " + nom + " (" + dateNaissance.getYear() + "-" + (dateDeces != null ? dateDeces.getYear() : "en vie") + ")";
    }
}