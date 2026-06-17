package com.usmb.but3.td4biblio.repository;

import java.util.List;
import com.usmb.but3.td4biblio.entity.Editeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditeurRepo extends JpaRepository<Editeur, Integer> {
    List<Editeur> findByNomContainingIgnoreCase(String nom);
}
