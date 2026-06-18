package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.*;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EditeurMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * La couche Service où se trouve toute la logique métier des éditeurs.
 * Elle interagit avec la couche Repository pour accéder aux données.
 * Gère les éditeurs et leur relation avec les adresses.
 */
@Service
@Transactional
public class EditeurService
        extends AbstractGenericService<Editeur, Integer, EditeurResponseDto, EditeurDetailResponseDto, EditeurCreateDto>{

    private final EditeurMapper mapper;
    private final AdresseRepo adresseRepo;

    /**
     * Constructeur du service Editeur.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des éditeurs
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param adresseRepo - repository des adresses
     */
    public EditeurService(EditeurRepo repository, EditeurMapper mapper, AdresseRepo adresseRepo) {
        super(repository, mapper);
        this.mapper = mapper;
        this.adresseRepo = adresseRepo;
    }

    /**
     * Met à jour un éditeur existant à partir des données du DTO.
     * Résout également la relation avec l'adresse.
     * @param id - identifiant de l'éditeur à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé de l'éditeur mis à jour
     * @throws RessourceNotFoundException si l'éditeur ou l'adresse n'existe pas
     */
    @Override
    public EditeurDetailResponseDto update(Integer id, EditeurCreateDto dto) {
        Editeur editeur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        mapper.updateFromDto(dto, editeur);

        Adresse adresse = adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvée : " + dto.getAdresseId()));

        editeur.setAdresse(adresse);

        return mapper.toDetailResponse(repository.save(editeur));
    }

    /**
     * Crée un nouvel éditeur à partir des données du DTO.
     * Résout également la relation avec l'adresse.
     * @param dto - données de création de l'éditeur
     * @return le DTO détaillé de l'éditeur créé
     * @throws RessourceNotFoundException si l'adresse spécifiée n'existe pas
     */
    @Override
    public EditeurDetailResponseDto create(EditeurCreateDto dto) {
        Editeur editeur = mapper.toEntity(dto);

        editeur.setAdresse(adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvée : " + dto.getAdresseId())));

        return mapper.toDetailResponse(repository.save(editeur));
    }
    public List<EditeurResponseDto> getByNomContainingIgnoreCase(String nom) {
        return ((EditeurRepo) repository).findByNomContainingIgnoreCase(nom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Integer resolveAdresseId(AdresseCreateDto adresseDto) {
        return adresseRepo.findByRueAndCodePostalAndVille(
                        adresseDto.getRue(),
                        adresseDto.getCodePostal(),
                        adresseDto.getVille())
                .map(Adresse::getId)
                .orElseGet(() -> {
                    Adresse nouvelle = new Adresse();
                    nouvelle.setRue(adresseDto.getRue());
                    nouvelle.setCodePostal(adresseDto.getCodePostal());
                    nouvelle.setVille(adresseDto.getVille());
                    return adresseRepo.save(nouvelle).getId();
                });
    }

}