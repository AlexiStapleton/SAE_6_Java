package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "editeur", schema = "biblio")
public class Editeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String nom;
    private String lienSiteWeb;
    private String lienWikipedia;

    @ManyToOne
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;
}