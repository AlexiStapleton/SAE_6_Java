package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.LivreMapper;
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
 private final LivreMapper livreMapper;

 public List<LivreResponseDto> getAllLivres(){
     //return livreRepo.findAll();
    // To specify a sort order, use:
      return livreRepo.findAll(Sort.by(Sort.Direction.ASC, "id"))
              .stream()
              .map(livreMapper::toResponse)
              .toList();

 }

 public LivreDetailResponseDto getLivreById(Integer id) {
    Livre livre = livreRepo.findById(id)
            .orElseThrow(() -> new RessourceNotFoundException("Le livre introuvable avec l'id : " + id));

    return livreMapper.toDetailResponse(livre);
 }

 public LivreResponseDto saveLivre (LivreCreateDto dto) {
     Livre livre = livreMapper.toEntity(dto);
     livre.setCreatedAt(LocalDateTime.now());
     livre.setUpdatedAt(LocalDateTime.now());

     Livre savedLivre = livreRepo.save(livre);
     log.info("Livre with id: {} saved successfully", livre.getId());
     return livreMapper.toResponse(savedLivre);
 }

 public LivreDetailResponseDto updateLivre (LivreDetailResponseDto dto) {
     Livre livre = livreRepo.findById(dto.getId())
             .orElseThrow(() -> new RessourceNotFoundException("Livre introuvable : " + dto.getId()));

     livre.setTitre(dto.getTitre());
     livre.setNbPages(dto.getNbPages());
     livre.setCodeIsbn(dto.getCodeIsbn());
     livre.setDatePublication(dto.getDatePublication());
     livre.setUpdatedAt(LocalDateTime.now());

     Livre updatedLivre = livreRepo.save(livre);

     return livreMapper.toDetailResponse(updatedLivre);
 }

 public void deleteLivreById (Integer id) {
    livreRepo.deleteById(id);
    log.info("Livre with id: {} deleted successfully", id);
 }

   public List<LivreResponseDto> getByAuteurId(Integer auteurId) {
      // Get livres by auteurId sorted by id
      //return livreRepo.findByAuteurId(auteurId);
      //how to sort by id
       return livreRepo.findByAuteurId(auteurId, Sort.by(Sort.Direction.ASC, "id"))
               .stream()
               .map(livreMapper::toResponse)
               .toList();
   }

   public List<LivreResponseDto> getByTitreContainingIgnoreCase(String titre) {
      return livreRepo.findByTitreContainingIgnoreCase(titre)
              .stream()
              .map(livreMapper::toResponse)
              .toList();
   }

}