package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeAuteurDetailResponseDto {
    private Integer Id;
    private String nom;
    private List<AuteurResponseDto> auteurs;
}
