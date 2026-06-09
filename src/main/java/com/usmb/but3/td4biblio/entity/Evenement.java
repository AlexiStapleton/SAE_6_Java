package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "evenement", schema = "biblio")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "bibliotheque_id", nullable = false)
    private Bibliotheque bibliotheque;

    @ManyToOne
    @JoinColumn(name = "type_evenement_id", nullable = false)
    private TypeEvenement typeEvenement;
}