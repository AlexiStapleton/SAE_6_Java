package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.Emprunt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpruntResponseDto {
    private Emprunt.EmpruntId id;
    private String nomUtilisateur;
    private String nomDocument;
    private LocalDate dateCreation;
    private LocalDate prolongation;
}
