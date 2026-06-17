package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuteurDetailResponseDto {
    private Integer Id;
    private String nom;
    private String prenom;
    private String nationalite;
    private LocalDate dateNaissance;
    private LocalDate dateDeces;
    private String villeNaissance;
    private String lienWikipedia;

    private List<TypeAuteurResponseDto> typesAuteur;
    private List<DocumentResponseDto> documents;
}
