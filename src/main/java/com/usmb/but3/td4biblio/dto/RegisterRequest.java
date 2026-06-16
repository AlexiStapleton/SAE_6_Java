package com.usmb.but3.td4biblio.dto;

import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Data
public class RegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    private String email;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    private String numeroCarte;

    @NotNull(message = "Le rôle est obligatoire")
    private RoleUtilisateur roleUtilisateur;

    // Champs adresse
    @NotBlank(message = "La rue est obligatoire")
    private String rue;

    @NotBlank(message = "Le code postal est obligatoire")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;
}