package com.usmb.but3.td4biblio.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliothequeRepo extends JpaRepository<Bibliotheque, Integer> {
    Page<Bibliotheque> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
