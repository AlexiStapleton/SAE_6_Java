package com.usmb.but3.td4biblio.dto;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdresseCreateDto {
        private String rue;
        private String codePostal;
        private String ville;
    }
}
