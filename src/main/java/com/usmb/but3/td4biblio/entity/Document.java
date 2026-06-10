package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "document", schema = "biblio")
@Inheritance(strategy = InheritanceType.JOINED)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private Auteur auteur;

    @ManyToOne
    @JoinColumn(name = "editeur_id")
    private Editeur editeur;

    @ManyToOne
    @JoinColumn(name = "bibliotheque_id")
    private Bibliotheque bibliotheque;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreDocument genre;

    @ManyToOne
    @JoinColumn(name = "code_raison_id")
    private CodeRaison codeRaison;

    private String titre;
    private String gif;
    private String format;
    private String description;
    private LocalDate dateAcquisition;
    private LocalDate datePublication;
    private String codeEmplacement;
    private Boolean empruntable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}