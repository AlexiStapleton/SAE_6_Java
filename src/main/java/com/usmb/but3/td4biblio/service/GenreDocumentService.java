package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.GenreDocumentCreateDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.GenreDocumentMapper;
import com.usmb.but3.td4biblio.repository.GenreDocumentRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * La couche Service où se trouve toute la logique métier des genres de documents.
 * Elle interagit avec la couche Repository pour accéder aux données.
 */
@Service
@Transactional
public class GenreDocumentService
        extends AbstractGenericService<GenreDocument, Integer, GenreDocumentResponseDto, GenreDocumentResponseDto, GenreDocumentCreateDto>{

    /**
     * Constructeur du service GenreDocument.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des genres de documents
     * @param mapper - mapper pour la conversion entre entités et DTOs
     */
    public GenreDocumentService(GenreDocumentRepo repository, GenreDocumentMapper mapper) {
        super(repository, mapper);
    }

    /**
     * Met à jour un genre de document existant à partir des données du DTO.
     * @param id - identifiant du genre de document à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO de réponse du genre de document mis à jour
     * @throws RessourceNotFoundException si le genre de document n'existe pas
     */
    public GenreDocumentResponseDto update(Integer id, GenreDocumentCreateDto dto) {
        GenreDocument genreDocument = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Genre non trouvé avec id : " + id));

        genreDocument.setNom(dto.getNom());

        return mapper.toResponse(repository.save(genreDocument));
    }

}