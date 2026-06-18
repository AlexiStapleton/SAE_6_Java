package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvenementResponseDto {
    private Integer Id;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String nomBibliotheque;
    private String nomTypeEvenement;
}
