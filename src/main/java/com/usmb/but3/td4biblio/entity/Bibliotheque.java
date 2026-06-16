package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bibliotheque", schema = "biblio")
public class Bibliotheque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String nom;

    @ManyToOne
    @JoinColumn(name = "adresse_id", nullable = false)
    private Adresse adresse;

    @OneToMany(mappedBy = "bibliotheque")
    private List<Evenement> evenements;

    @OneToMany(mappedBy = "bibliotheque")
    private List<Document> documents;
}