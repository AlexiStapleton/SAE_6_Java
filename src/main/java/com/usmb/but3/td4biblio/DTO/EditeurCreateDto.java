package com.usmb.but3.td4biblio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditeurCreateDto {
    private String nom;
    private String lienSiteWeb;
    private String lienWikipedia;
    private int adresseId;
}
