package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Evenement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvenementRepo extends JpaRepository<Evenement, Integer> {
    Page<Evenement> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
