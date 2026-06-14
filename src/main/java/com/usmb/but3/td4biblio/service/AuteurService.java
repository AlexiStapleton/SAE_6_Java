package com.usmb.but3.td4biblio.service;

import java.util.List;

import com.usmb.but3.td4biblio.dto.AuteurCreateDto;
import com.usmb.but3.td4biblio.dto.AuteurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.AuteurMapper;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.repository.AuteurRepo;

import lombok.RequiredArgsConstructor;

/**
 * La couche Service où se trouve toute la logique métier de l'application.
 * Elle interagit avec la couche Repository pour accéder aux données.
 */
@Service
@Transactional
public class AuteurService
    extends AbstractGenericService<Auteur, Integer, AuteurResponseDto, AuteurDetailResponseDto, AuteurCreateDto>{

    private final AuteurRepo auteurRepo;  // ← version typée en plus
    private final TypeAuteurRepo typeAuteurRepo;
    private final AuteurMapper mapper;

    /**
     * Constructeur du service Auteur.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des auteurs
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param typeAuteurRepo - repository des types d'auteur
     */
    public AuteurService(AuteurRepo repository, AuteurMapper mapper, TypeAuteurRepo typeAuteurRepo) {
        super(repository, mapper);
        this.auteurRepo = repository;  // ← même instance, juste typée correctement
        this.typeAuteurRepo = typeAuteurRepo;
        this.mapper = mapper;
    }

    /**
     * Met à jour un auteur existant à partir des données du DTO.
     * Résout également la relation avec les types d'auteur.
     * @param id - identifiant de l'auteur à mettre à jour
     * @param dto - données de mise à jour
     * @return le détail de l'auteur mis à jour
     */
    @Override
    public AuteurDetailResponseDto update(Integer id,  AuteurCreateDto dto) {

        Auteur auteur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        mapper.updateFromDto(dto, auteur);

        List<TypeAuteur> types = typeAuteurRepo.findAllById(dto.getTypesAuteurIds());
        auteur.setTypesAuteur(types);

        return mapper.toDetailResponse(repository.save(auteur));
    }

    /**
     * Récupère tous les auteurs dont le nom correspond exactement à la valeur donnée.
     * @param nom - nom exact à rechercher
     * @return liste des auteurs correspondants
     */
    public List<AuteurResponseDto> getAuteursByNom(String nom) {

        return auteurRepo.findByNom(nom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Récupère tous les auteurs dont le nom et le prénom correspondent exactement aux valeurs données.
     * @param nom - nom exact à rechercher
     * @param prenom - prénom exact à rechercher
     * @return liste des auteurs correspondants
     */
    public List<AuteurResponseDto> getAuteursByNomAndPrenom(String nom, String prenom) {
        return auteurRepo.findByNomAndPrenom(nom, prenom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Récupère tous les auteurs dont le nom et le prénom correspondent au motif donné.
     * @param nom - motif de recherche sur le nom
     * @param prenom - motif de recherche sur le prénom
     * @return liste des auteurs correspondants
     */
    public List<AuteurResponseDto> getAuteursByNomLikeAndPrenomLike(String nom, String prenom) {
        //return auteurRepo.findByNomLikeAndPrenomLike("%" + nom + "%", "%" + prenom + "%");
        return auteurRepo.findByNomLikeAndPrenomLike(nom,prenom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Récupère tous les auteurs dont le nom commence par le filtre donné, sans tenir compte de la casse.
     * @param filter - début du nom à rechercher
     * @return liste des auteurs correspondants
     */
    public List<AuteurResponseDto> getAuteursByNomStartWithIgnoreCase(String filter) {
        return auteurRepo.findByNomStartsWithIgnoreCase(filter)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Récupère tous les auteurs dont le nom contient le filtre donné, sans tenir compte de la casse.
     * @param filter - chaîne à rechercher dans le nom
     * @return liste des auteurs correspondants
     */
    public List<AuteurResponseDto> getByNomContainingIgnoreCase(String filter) {
       return auteurRepo.findByNomContainingIgnoreCase(filter)
               .stream()
               .map(mapper::toResponse)
               .toList();
    }

    public List<Auteur> getAllAuteurEntities() {
        return auteurRepo.findAll(
            Sort.by(Sort.Direction.ASC, "id")
        );
    }

}
