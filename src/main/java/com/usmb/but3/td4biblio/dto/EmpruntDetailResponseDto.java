package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.Emprunt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpruntDetailResponseDto {
    private Emprunt.EmpruntId id;
    private UtilisateurResponseDto utilisateur;
    private DocumentResponseDto document;
    private LocalDate dateCreation;
    private LocalDate dateFin;
    private LocalDate prolongation;
}
