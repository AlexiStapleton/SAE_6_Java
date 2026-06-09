package com.usmb.but3.td4biblio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.repository.LivreRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * La couche Service où se trouve toute la logique métier de l'application.
 * Elle interagit avec la couche Repository pour accéder aux données.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LivreService {

 private final LivreRepo livreRepo;

 public List<Livre> getAllLivres(){
     //return livreRepo.findAll();
    // To specify a sort order, use:
      return livreRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));

 }

 public Livre getLivreById(Integer id) {
    Optional<Livre> optionalLivre = livreRepo.findById(id);
    log.debug("id: {}", id);
    if(optionalLivre.isPresent()){
        return optionalLivre.get();
    }
    log.error("Livre with id: {} doesn't exist", id);
    return null;
 }

 public Livre saveLivre (Livre livre) {
    livre.setCreatedAt(LocalDateTime.now());
    livre.setUpdatedAt(LocalDateTime.now());
    Livre savedLivre = livreRepo.save(livre);

    log.info("Livre with id: {} saved successfully", livre.getId());
    return savedLivre;
 }

 public Livre updateLivre (Livre livre) {
    Optional<Livre> existingLivre = livreRepo.findById(livre.getId());
    livre.setCreatedAt(existingLivre.get().getCreatedAt());
    livre.setUpdatedAt(LocalDateTime.now());

    Livre updatedLivre = livreRepo.save(livre);

    log.info("Livre with id: {} updated successfully", livre.getId());
    return updatedLivre;
 }

 public void deleteLivreById (Integer id) {
    livreRepo.deleteById(id);
    log.info("Livre with id: {} deleted successfully", id);
 }

   public List<Livre> getByAuteurId(Integer auteurId) {
      // Get livres by auteurId sorted by id
      //return livreRepo.findByAuteurId(auteurId);
      //how to sort by id
       return livreRepo.findByAuteurId(auteurId, Sort.by(Sort.Direction.ASC, "id")); 
   }

   public List<Livre> getByTitreContainingIgnoreCase(String titre) {
      return livreRepo.findByTitreContainingIgnoreCase(titre);
   }

}