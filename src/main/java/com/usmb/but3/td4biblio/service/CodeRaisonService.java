package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.CodeRaisonCreateDto;
import com.usmb.but3.td4biblio.dto.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.entity.CodeRaison;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.CodeRaisonMapper;
import com.usmb.but3.td4biblio.repository.CodeRaisonRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * La couche Service où se trouve toute la logique métier des codes raison.
 * Elle interagit avec la couche Repository pour accéder aux données.
 */
@Service
@Transactional
public class CodeRaisonService
        extends AbstractGenericService<CodeRaison, Integer, CodeRaisonResponseDto, CodeRaisonResponseDto, CodeRaisonCreateDto> {

    /**
     * Constructeur du service CodeRaison.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des codes raison
     * @param mapper - mapper pour la conversion entre entités et DTOs
     */
    public CodeRaisonService(CodeRaisonRepo repository, CodeRaisonMapper mapper) {
        super(repository, mapper);
    }

    /**
     * Met à jour un code raison existant à partir des données du DTO.
     * @param id - identifiant du code raison à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO de réponse du code raison mis à jour
     * @throws RessourceNotFoundException si le code raison n'existe pas
     */
    @Override
    public CodeRaisonResponseDto update(Integer id, CodeRaisonCreateDto dto) {
        CodeRaison codeRaison = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Pas de code Raison avec l'id" + id));

        mapper.updateFromDto(dto, codeRaison);

        return mapper.toDetailResponse(repository.save(codeRaison));
    }
}