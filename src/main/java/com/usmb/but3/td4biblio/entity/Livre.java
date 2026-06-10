package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
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
@Table(name = "livre", schema = "biblio")
@PrimaryKeyJoinColumn(name = "id")
public class Livre extends Document {

    private Integer nbPages;
    private String codeIsbn;

    public boolean isEqualTo(Livre livre) {
        if (this == livre) return true;
        if (livre == null) return false;
        if (getId() != null ? !getId().equals(livre.getId()) : livre.getId() != null) return false;
        if (getTitre() != null ? !getTitre().equals(livre.getTitre()) : livre.getTitre() != null) return false;
        if (getAuteur() != null ? !getAuteur().equals(livre.getAuteur()) : livre.getAuteur() != null) return false;
        if (nbPages != null ? !nbPages.equals(livre.nbPages) : livre.nbPages != null) return false;
        if (getDatePublication() != null ? !getDatePublication().equals(livre.getDatePublication()) : livre.getDatePublication() != null) return false;
        return codeIsbn != null ? codeIsbn.equals(livre.codeIsbn) : livre.codeIsbn == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isEqualTo((Livre) obj);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getId() == null ? 0 : getId().hashCode());
        result = prime * result + (getTitre() == null ? 0 : getTitre().hashCode());
        result = prime * result + (getAuteur() == null ? 0 : getAuteur().hashCode());
        result = prime * result + (nbPages == null ? 0 : nbPages.hashCode());
        result = prime * result + (getDatePublication() == null ? 0 : getDatePublication().hashCode());
        result = prime * result + (codeIsbn == null ? 0 : codeIsbn.hashCode());
        return result;
    }
}
