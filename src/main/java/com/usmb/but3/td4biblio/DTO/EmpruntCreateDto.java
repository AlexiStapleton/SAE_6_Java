package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpruntCreateDto {
    private Integer documentId;
    private Integer utilisateurId;
    private LocalDate prolongation;
}
