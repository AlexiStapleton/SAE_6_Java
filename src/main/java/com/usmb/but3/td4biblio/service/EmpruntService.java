package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EmpruntCreateDto;
import com.usmb.but3.td4biblio.dto.EmpruntDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EmpruntResponseDto;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EmpruntMapper;
import com.usmb.but3.td4biblio.repository.UserRepository;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmpruntRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * La couche Service où se trouve toute la logique métier des emprunts.
 * Elle interagit avec la couche Repository pour accéder aux données.
 * Gère les emprunts et leur relation avec les documents et utilisateurs.
 */
@Service
@Transactional
public class EmpruntService extends AbstractGenericService<Emprunt, Emprunt.EmpruntId, EmpruntResponseDto, EmpruntDetailResponseDto, EmpruntCreateDto> {

    private final UserRepository utilisateurRepository;
    private final DocumentRepo documentRepository;

    /**
     * Constructeur du service Emprunt.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des emprunts
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param utilisateurRepository - repository des utilisateurs
     * @param documentRepository - repository des documents
     */
    public EmpruntService(EmpruntRepo repository, EmpruntMapper mapper, UserRepository utilisateurRepository, DocumentRepo documentRepository){
        super(repository, mapper);
        this.utilisateurRepository = utilisateurRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * Met à jour un emprunt existant à partir des données du DTO.
     * Résout également les relations avec le document et l'utilisateur.
     * @param id - identifiant composite de l'emprunt à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé de l'emprunt mis à jour
     * @throws RessourceNotFoundException si l'emprunt, le document ou l'utilisateur n'existe pas
     */
    @Override
    public EmpruntDetailResponseDto update(Emprunt.EmpruntId id, EmpruntCreateDto dto){
        Emprunt emprunt = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Emprunt non trouvé : " + id));

        mapper.updateFromDto(dto , emprunt);
        emprunt.setDocument(documentRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + dto.getDocumentId())));
        emprunt.setUtilisateur(utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé : " + dto.getUtilisateurId())));

        return mapper.toDetailResponse(repository.save(emprunt));
    }

    /**
     * Crée un nouvel emprunt à partir des données du DTO.
     * Résout également les relations avec le document et l'utilisateur.
     * La date de création est automatiquement définie à la date du jour.
     * @param dto - données de création de l'emprunt
     * @return le DTO détaillé de l'emprunt créé
     * @throws RessourceNotFoundException si le document ou l'utilisateur spécifiés n'existent pas
     */
    @Override
    public EmpruntDetailResponseDto create(EmpruntCreateDto dto) {
        Emprunt emprunt = mapper.toEntity(dto);


        emprunt.setId(new Emprunt.EmpruntId(dto.getUtilisateurId(), dto.getDocumentId()));

        emprunt.setDocument(documentRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + dto.getDocumentId())));
        emprunt.setUtilisateur(utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RessourceNotFoundException("utilisateur non trouvé : " + dto.getUtilisateurId())));

        emprunt.setDateCreation(LocalDate.now());

        return mapper.toDetailResponse(repository.save(emprunt));

    }
}