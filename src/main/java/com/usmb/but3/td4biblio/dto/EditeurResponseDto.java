package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.Adresse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
