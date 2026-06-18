package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Evenement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;


public interface EvenementRepo extends JpaRepository<Evenement, Integer> {
    Page<Evenement> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    /**
     * Retourne les 5 prochains événements (date de début >= la date donnée),
     * triés par date de début croissante. Utilisé par la page d'accueil.
     */
    List<Evenement> findTop5ByDateDebutGreaterThanEqualOrderByDateDebutAsc(LocalDate date);

}
