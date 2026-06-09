package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Auteur;

public interface AuteurRepo extends JpaRepository<Auteur, Integer> {
    List<Auteur> findByNom(String nom);
    List<Auteur> findByNomAndPrenom(String nom, String prenom);
    List<Auteur> findByNomLikeAndPrenomLike(String nom, String prenom);
    List<Auteur> findByNomStartsWithIgnoreCase(String filterText);
    List<Auteur> findByNomContainingIgnoreCase(String filter);
}