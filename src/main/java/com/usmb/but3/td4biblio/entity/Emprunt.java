package com.usmb.but3.td4biblio.entity;

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
@IdClass(Emprunt.EmpruntId.class)
public class Emprunt {

    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Id
    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    private LocalDate dateCreation;
    private LocalDate prolongation;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class EmpruntId implements Serializable {
        private Integer utilisateurId;
        private Integer documentId;
    }
}