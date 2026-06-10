package com.usmb.but3.td4biblio.entity;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "emprunt", schema = "biblio")
public class Emprunt {

    @EmbeddedId
    private EmpruntId id;

    @ManyToOne
    @MapsId("utilisateurId")
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @MapsId("documentId")
    @JoinColumn(name = "document_id")
    private Document document;

    private LocalDate dateCreation;
    private LocalDate prolongation;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class EmpruntId implements Serializable {
        private Integer utilisateurId;
        private Integer documentId;
    }
}