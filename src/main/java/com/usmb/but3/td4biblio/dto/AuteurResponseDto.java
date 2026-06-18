package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuteurResponseDto {
    private Integer Id;
    private String nom;
    private String prenom;
    private String nationalite;
    private LocalDate dateNaissance;
    private LocalDate dateDeces;
    private String villeNaissance;
    private String lienWikipedia;

    public String getDesc() {
        return prenom + " " + nom + " (" + dateNaissance.getYear() + "-" + (dateDeces != null ? dateDeces.getYear() : "en vie") + ")";
    }
}
