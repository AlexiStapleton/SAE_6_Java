package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvenementDetailResponseDto {
    private Integer Id;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private BibliothequeResponseDto bibliotheque;
    private TypeEvenementResponseDto typeEvenement;
}
