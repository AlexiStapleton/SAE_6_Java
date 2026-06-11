package com.usmb.but3.td4biblio.repository;


import com.usmb.but3.td4biblio.entity.Utilisateur;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Integer> {
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByEmail(String email);
}
