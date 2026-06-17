package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditeurResponseDto {
    private Integer Id;
    private String nom;
    private String lienSiteWeb;
    private String lienWikipedia;
    private AdresseResponseDto adresse;
}
