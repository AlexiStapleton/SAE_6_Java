package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.LivreMapper;
import jakarta.transaction.Transactional;
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
@Transactional
public class LivreService
    extends AbstractGenericService<Livre, Integer, LivreResponseDto, LivreDetailResponseDto, LivreCreateDto>{

 private final LivreRepo livreRepo;

 public LivreService(LivreRepo repository, LivreMapper mapper) {
     super(repository, mapper);
     this.livreRepo = repository;
 }

 public LivreDetailResponseDto update (LivreDetailResponseDto dto) {
     Livre livre = livreRepo.findById(dto.getId())
             .orElseThrow(() -> new RessourceNotFoundException("Livre introuvable : " + dto.getId()));

     livre.setTitre(dto.getTitre());
     livre.setNbPages(dto.getNbPages());
     livre.setCodeIsbn(dto.getCodeIsbn());
     livre.setDatePublication(dto.getDatePublication());
     livre.setUpdatedAt(LocalDateTime.now());

     Livre updatedLivre = livreRepo.save(livre);

     return mapper.toDetailResponse(updatedLivre);
 }

 @Override
 public LivreResponseDto update(Integer id, LivreCreateDto dto) {
     Livre livre = repository.findById(id)
             .orElseThrow(() -> new RessourceNotFoundException("Livre non trouvé avec id : " + id));

     return mapper.toResponse(livre);
 }

// public void deleteLivreById (Integer id) {
//    livreRepo.deleteById(id);
//    log.info("Livre with id: {} deleted successfully", id);
// }

   public List<LivreResponseDto> getByAuteurId(Integer auteurId) {
      // Get livres by auteurId sorted by id
      //return livreRepo.findByAuteurId(auteurId);
      //how to sort by id
       return livreRepo.findByAuteurId(auteurId, Sort.by(Sort.Direction.ASC, "id"))
               .stream()
               .map(mapper::toResponse)
               .toList();
   }

   public List<LivreResponseDto> getByTitreContainingIgnoreCase(String titre) {
      return livreRepo.findByTitreContainingIgnoreCase(titre)
              .stream()
              .map(mapper::toResponse)
              .toList();
   }

}