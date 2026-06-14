package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurResponseDto {
    private Integer Id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;
    private LocalDate dateFinAbonnement;
    private String numeroCarte;
    private AdresseResponseDto adresse;
    private RoleUtilisateur roleUtilisateur;
}
