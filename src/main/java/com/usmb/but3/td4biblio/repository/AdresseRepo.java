package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdresseRepo extends JpaRepository<Adresse, Integer> {
    Optional<Adresse> findByRueAndCodePostalAndVille(String rue, String codePostal, String ville);
}