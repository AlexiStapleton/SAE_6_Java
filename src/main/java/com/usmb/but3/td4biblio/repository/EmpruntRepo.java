package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpruntRepo extends JpaRepository<Emprunt, Emprunt.EmpruntId> {

    List<Emprunt> findEmpruntByUtilisateur_Email(String email);

    // Nombre d'emprunts actifs d'un utilisateur (dateFin dans le futur ou null)
    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.utilisateur.Id = :utilisateurId AND (e.dateFin IS NULL OR e.dateFin >= CURRENT_DATE)")
    long countEmpruntsActifsByUtilisateur(@Param("utilisateurId") Integer utilisateurId);

    // Vérifie si un document est actuellement emprunté
    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.document.Id = :documentId AND (e.dateFin IS NULL OR e.dateFin >= CURRENT_DATE)")
    long countEmpruntsActifsByDocument(@Param("documentId") Integer documentId);

    @Query("""
    SELECT e.document.Id FROM Emprunt e 
    WHERE (e.dateFin IS NULL OR e.dateFin >= CURRENT_DATE)
    AND (e.prolongation IS NULL OR e.prolongation >= CURRENT_DATE)
    """)
    List<Integer> findDocumentIdsActuellementEmpruntes();

    @Query("""
    SELECT e.utilisateur.Id FROM Emprunt e 
    WHERE (e.dateFin IS NULL OR e.dateFin >= CURRENT_DATE)
    AND (e.prolongation IS NULL OR e.prolongation >= CURRENT_DATE)
    GROUP BY e.utilisateur.Id HAVING COUNT(e) >= 10
    """)
    List<Integer> findUtilisateurIdsAyantAtteintLimite();
}
