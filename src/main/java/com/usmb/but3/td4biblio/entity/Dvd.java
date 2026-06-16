package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "dvd", schema = "biblio")
@PrimaryKeyJoinColumn(name = "id")
public class Dvd extends Document {

    private Integer duree;
}