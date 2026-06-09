package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "type_evenement", schema = "biblio")
public class TypeEvenement {

    @Id
    private Integer id;
    private String nom;
}