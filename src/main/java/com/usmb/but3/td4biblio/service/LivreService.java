package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.LivreMapper;
import com.usmb.but3.td4biblio.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Livre;

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
    extends AbstractDocumentService<Livre, LivreResponseDto, LivreDetailResponseDto, LivreCreateDto>{

    private final LivreRepo livreRepo;

    /**
     * Constructeur du service Livre.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des livres
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param auteurRepository - repository des auteurs
     * @param editeurRepository - repository des éditeurs
     * @param bibliothequeRepository - repository des bibliothèques
     * @param genreRepository - repository des genres
     * @param codeRaisonRepository - repository des codes raison
     */
    public LivreService(LivreRepo repository,
                        LivreMapper mapper,
                        AuteurRepo auteurRepository,
                        EditeurRepo editeurRepository,
                        BibliothequeRepo bibliothequeRepository,
                        GenreDocumentRepo genreRepository,
                        CodeRaisonRepo codeRaisonRepository) {
        super(repository,
             mapper,
             auteurRepository,
             editeurRepository,
             bibliothequeRepository,
             genreRepository,
             codeRaisonRepository);
     livreRepo = repository;
 }


    /**
     * Récupère tous les livres d'un auteur donné, triés par identifiant croissant.
     * @param auteurId - identifiant de l'auteur
     * @return liste des livres de l'auteur
     */
   public List<LivreResponseDto> getByAuteurId(Integer auteurId) {
      // Get livres by auteurId sorted by id
      //return livreRepo.findByAuteurId(auteurId);
      //how to sort by id
       return livreRepo.findByAuteurId(auteurId, Sort.by(Sort.Direction.ASC, "id"))
               .stream()
               .map(mapper::toResponse)
               .toList();
   }

    /**
     * Récupère tous les livres dont le titre contient la chaîne donnée, sans tenir compte de la casse.
     * @param titre - titre complet ou partiel à rechercher
     * @return liste des livres correspondant au titre recherché
     */
   public List<LivreResponseDto> getByTitreContainingIgnoreCase(String titre) {
      return livreRepo.findByTitreContainingIgnoreCase(titre)
              .stream()
              .map(mapper::toResponse)
              .toList();
   }

}