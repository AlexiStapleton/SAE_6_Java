package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDetailResponseDto {
    private Integer Id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;
    private LocalDate dateFinAbonnement;
    private String numeroCarte;
    private AdresseResponseDto adresse;
    private List<EmpruntResponseDto> emprunts;
    private RoleUtilisateur roleUtilisateur;
}
