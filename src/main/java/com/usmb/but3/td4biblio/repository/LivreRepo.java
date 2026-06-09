package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Livre;

/**
 * Repository is an interface that provides access to data in a database
 */
public interface LivreRepo extends JpaRepository<Livre, Integer> {

    List<Livre> findByAuteurId(Integer auteurId);
    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByAuteurId(Integer auteurId, Sort sort); 
}