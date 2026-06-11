package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "utilisateur", schema = "biblio")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;
    private LocalDate dateFinAbonnement;
    private String numeroCarte;
    private String hashMotDePasse;



    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role_utilisateur", columnDefinition = "biblio.role_utilisateur")
    private RoleUtilisateur roleUtilisateur;

    public enum RoleUtilisateur {
        BIBLIOTHECAIRE, EMPRUNTEUR
    }
}
