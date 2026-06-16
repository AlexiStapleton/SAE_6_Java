package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseResponseDto {
    private Integer Id;
    private String rue;
    private String codePostal;
    private String ville;
}
