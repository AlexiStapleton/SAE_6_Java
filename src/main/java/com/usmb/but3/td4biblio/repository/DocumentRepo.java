package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepo extends JpaRepository<Document, Integer> {

    List<Document> findByTitreContainingIgnoreCase(String titre);

    List<Document> findByTitreStartsWithIgnoreCase(String titre);

    List<Document> findByTitreIgnoreCase(String titre);

    List<Document> findByAuteurId(Integer auteurId);

    List<Document> findByAuteurId(Integer auteurId, Sort sort);

    // Auteur
    List<Document> findByAuteurNomContainingIgnoreCase(String nom);
    List<Document> findByAuteurNomStartsWithIgnoreCase(String nom);
    List<Document> findByAuteurNomIgnoreCase(String nom);


    // Bibliothèque
    List<Document> findByBibliothequeNomContainingIgnoreCase(String nom);
    List<Document> findByBibliothequeNomStartsWithIgnoreCase(String nom);
    List<Document> findByBibliothequeNomIgnoreCase(String nom);


    // Editeur
    List<Document> findByEditeurNomContainingIgnoreCase(String nom);
    List<Document> findByEditeurNomStartsWithIgnoreCase(String nom);
    List<Document> findByEditeurNomIgnoreCase(String nom);


    // Genre
    List<Document> findByGenreNomContainingIgnoreCase(String nom);
    List<Document> findByGenreNomStartsWithIgnoreCase(String nom);
    List<Document> findByGenreNomIgnoreCase(String nom);

    List<Document> findAllByOrderByTitreAsc();
}