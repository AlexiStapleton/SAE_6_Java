package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.Adresse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditeurDetailResponseDto {
    private Integer Id;
    private String nom;
    private String lienSiteWeb;
    private String lienWikipedia;
    private AdresseResponseDto adresse;
    private List<DocumentResponseDto> documents;
}
