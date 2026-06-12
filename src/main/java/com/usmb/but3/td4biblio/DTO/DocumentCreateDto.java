package com.usmb.but3.td4biblio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCreateDto {
    private AuteurResponseDto auteur;
    private EditeurResponseDto editeur;
    private BibliothequeResponseDto bibliotheque;
    private GenreDocumentResponseDto genre;
    private CodeRaisonResponseDto codeRaison;
    private String titre;
    private String gif;
    private String format;
    private String description;
    private LocalDate dateAcquisition;
    private LocalDate datePublication;
    private String codeEmplacement;
    private Boolean empruntable;
}
