package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpruntRepo extends JpaRepository<Emprunt, Emprunt.EmpruntId> {
}
