package com.usmb.but3.td4biblio.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.usmb.but3.td4biblio.enumeration.TypeDocument;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentDetailResponseDto {
    private Integer Id;
    private TypeDocument type;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
