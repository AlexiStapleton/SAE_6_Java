package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepo extends JpaRepository<Utilisateur, Integer> {
}
