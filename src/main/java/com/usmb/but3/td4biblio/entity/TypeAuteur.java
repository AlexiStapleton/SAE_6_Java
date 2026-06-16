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
@Table(name = "type_auteur", schema = "biblio")
public class TypeAuteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String nom;

    @ManyToMany(mappedBy = "typesAuteur")
    private List<Auteur> auteurs;
}