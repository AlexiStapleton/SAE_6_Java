package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "code_raison", schema = "biblio")
public class CodeRaison {

    @Id
    private Integer Id;
    private String nom;
    private String description;
}