package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibliothequeDetailResponseDto {
    private Integer Id;
    private String nom;
    private AdresseResponseDto adresse;
    private List<EvenementResponseDto> evenements;
    private List<DocumentResponseDto> documents;
}
