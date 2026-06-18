package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.GenericMapper;
import com.usmb.but3.td4biblio.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service générique abstrait pour la gestion des documents.
 * Fournit la logique métier commune à tous les types de documents.
 * Elle interagit avec la couche Repository pour accéder aux données et résout les relations avec les auteurs, éditeurs, bibliothèques, genres et codes raison.
 *
 * @param <T> - type de l'entité document (doit étendre Document)
 * @param <RespDto> - type du DTO de réponse
 * @param <DetailDto> - type du DTO détaillé
 * @param <CreateDto> - type du DTO de création/modification
 */
@Transactional
public abstract class AbstractDocumentService<T extends Document, RespDto extends DocumentResponseDto, DetailDto extends DocumentDetailResponseDto, CreateDto extends DocumentCreateDto>
        extends AbstractGenericService<T, Integer, RespDto, DetailDto, CreateDto>{

    private final AuteurRepo auteurRepository;
    private final EditeurRepo editeurRepository;
    private final BibliothequeRepo bibliothequeRepository;
    private final GenreDocumentRepo genreRepository;
    private final CodeRaisonRepo codeRaisonRepository;

    /**
     * Constructeur du service abstrait de document.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait parent.
     * @param repository - repository des documents
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param auteurRepository - repository des auteurs
     * @param editeurRepository - repository des éditeurs
     * @param bibliothequeRepository - repository des bibliothèques
     * @param genreRepository - repository des genres de documents
     * @param codeRaisonRepository - repository des codes raison
     */
    protected AbstractDocumentService(
            JpaRepository<T, Integer> repository,
            GenericMapper<T, RespDto, DetailDto, CreateDto> mapper,
            AuteurRepo auteurRepository,
            EditeurRepo editeurRepository,
            BibliothequeRepo bibliothequeRepository,
            GenreDocumentRepo genreRepository,
            CodeRaisonRepo codeRaisonRepository) {
        super(repository, mapper);
        this.auteurRepository = auteurRepository;
        this.editeurRepository = editeurRepository;
        this.bibliothequeRepository = bibliothequeRepository;
        this.genreRepository = genreRepository;
        this.codeRaisonRepository = codeRaisonRepository;
    }

    /**
     * Crée un nouveau document à partir des données du DTO.
     * Résout les relations avec l'auteur, l'éditeur, la bibliothèque, le genre et le code raison.
     * Les timestamps de création et modification sont automatiquement définis à la date/heure actuelle.
     * @param dto - données de création du document
     * @return le DTO détaillé du document créé
     * @throws RessourceNotFoundException si l'auteur, l'éditeur, la bibliothèque, le genre ou le code raison spécifiés n'existent pas
     */
    @Override
    public DetailDto create(CreateDto dto){

        T entity = mapper.toEntity(dto);

        entity.setAuteur(auteurRepository.findById(dto.getAuteurId())
                .orElseThrow(() -> new RessourceNotFoundException("Auteur non trouvé : " + dto.getAuteurId())));
        entity.setEditeur(editeurRepository.findById(dto.getEditeurId())
                .orElseThrow(() -> new RessourceNotFoundException("Editeur non trouvé : " + dto.getEditeurId())));
        entity.setBibliotheque(bibliothequeRepository.findById(dto.getBibliothequeId())
                .orElseThrow(() -> new RessourceNotFoundException("Bibliotheque non trouvé : " + dto.getBibliothequeId())));
        entity.setGenre(genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new RessourceNotFoundException("Genre non trouvé : " + dto.getGenreId())));
        if (dto.getCodeRaisonId() != null) {
            entity.setCodeRaison(codeRaisonRepository.findById(dto.getCodeRaisonId())
                    .orElseThrow(() -> new RessourceNotFoundException("Code Raison non trouvé : " + dto.getCodeRaisonId())));
        } else {
            entity.setCodeRaison(null);
        }

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDetailResponse(repository.save(entity));
    }

    /**
     * Met à jour un document existant à partir des données du DTO.
     * Résout les relations avec l'auteur, l'éditeur, la bibliothèque, le genre et le code raison.
     * Le timestamp de modification est automatiquement mis à jour à la date/heure actuelle.
     * @param id - identifiant du document à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé du document mis à jour
     * @throws RessourceNotFoundException si le document, l'auteur, l'éditeur, la bibliothèque, le genre ou le code raison n'existe pas
     */
    @Override
    public DetailDto update(Integer id, CreateDto dto) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        mapper.updateFromDto(dto, entity);

        entity.setAuteur(auteurRepository.findById(dto.getAuteurId())
                .orElseThrow(() -> new RessourceNotFoundException("Auteur non trouvé : " + dto.getAuteurId())));
        entity.setEditeur(editeurRepository.findById(dto.getEditeurId())
                .orElseThrow(() -> new RessourceNotFoundException("Editeur non trouvé : " + dto.getEditeurId())));
        entity.setBibliotheque(bibliothequeRepository.findById(dto.getBibliothequeId())
                .orElseThrow(() -> new RessourceNotFoundException("Bibliotheque non trouvé : " + dto.getBibliothequeId())));
        entity.setGenre(genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new RessourceNotFoundException("Genre non trouvé : " + dto.getGenreId())));
        if (dto.getCodeRaisonId() != null) {
            entity.setCodeRaison(codeRaisonRepository.findById(dto.getCodeRaisonId())
                    .orElseThrow(() -> new RessourceNotFoundException("Code Raison non trouvé : " + dto.getCodeRaisonId())));
        } else {
            entity.setCodeRaison(null);
        }

        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDetailResponse(repository.save(entity));
    }

}