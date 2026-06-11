package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliothequeRepo extends JpaRepository<Bibliotheque, Integer> {
}
