package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.enumeration.TypeDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFormDto {

    private TypeDocument typeDocument;

    private Integer auteurId;
    private Integer editeurId;
    private Integer bibliothequeId;
    private Integer genreId;
    private Integer codeRaisonId;

    private String titre;
    private String gif;
    private String format;
    private String description;
    private LocalDate dateAcquisition;
    private LocalDate datePublication;
    private String codeEmplacement;
    private Boolean empruntable;

    // Livre
    private Integer nbPages;
    private String codeIsbn;

    // DVD
    private Integer duree;
}