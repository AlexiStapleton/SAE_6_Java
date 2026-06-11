package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDto {
    private Integer Id;
    private String nomEditeur;
    private String nomAuteur;
    private String nomBibliotheque;
    private String nomGenreDocument;
    private String nomRaison;
    private String titre;
    private String gif;
    private String format;
    private String description;
    private LocalDate dateAcquisition;
    private LocalDate datePublication;
    private String codeEmplacement;
    private Boolean empruntable;

}
