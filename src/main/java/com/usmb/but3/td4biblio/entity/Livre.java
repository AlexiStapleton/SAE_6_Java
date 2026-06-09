package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une classe entité qui représente une table de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "livre")

public class Livre {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id;
 private String titre;
 private Integer nbPages;
 private String editeur;
 private LocalDate datePublication;

 private LocalDateTime createdAt;
 private LocalDateTime updatedAt;

@ManyToOne
@JoinColumn(name = "auteur_id", nullable = false) // Foreign key column in 'livre' table, not nullable
private Auteur auteur;

 public boolean isEqualTo(Livre livre) {
    if (this == livre) return true;
    if (livre == null) return false;
    if (id != null ? !id.equals(livre.id) : livre.id != null) return false;
    if (titre != null ? !titre.equals(livre.titre) : livre.titre != null) return false;
    if (auteur != null ? !auteur.equals(livre.auteur) : livre.auteur != null) return false;
    if (nbPages != null ? !nbPages.equals(livre.nbPages) : livre.nbPages != null) return false;
    if (editeur != null ? !editeur.equals(livre.editeur) : livre.editeur != null) return false;
    if (datePublication != null ? !datePublication.equals(livre.datePublication) : livre.datePublication != null) return false;
    return true;
 }

 @Override
 public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    Livre other = (Livre) obj;
    if (id == null) {
        if (other.id != null)
            return false;
    } else if (!id.equals(other.id))
        return false;
    if (titre == null) {
        if (other.titre != null)
            return false;
    } else if (!titre.equals(other.titre))
        return false;
    if (auteur == null) {
        if (other.auteur != null)
            return false;
    } else if (!auteur.equals(other.auteur))
        return false;
    if (nbPages == null) {
        if (other.nbPages != null)
            return false;
    } else if (!nbPages.equals(other.nbPages))
        return false;
    if (editeur == null) {
        if (other.editeur != null)
            return false;
    } else if (!editeur.equals(other.editeur))
        return false;
    if (datePublication == null) {
        if (other.datePublication != null)
            return false;
    } else if (!datePublication.equals(other.datePublication))
        return false;
    return true;
 }
 @Override
 public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((titre == null) ? 0 : titre.hashCode());
    result = prime * result + ((auteur == null) ? 0 : auteur.hashCode());
    result = prime * result + ((nbPages == null) ? 0 : nbPages.hashCode());
    result = prime * result + ((editeur == null) ? 0 : editeur.hashCode());
    result = prime * result + ((datePublication == null) ? 0 : datePublication.hashCode());
    return result;
 }

 
}
