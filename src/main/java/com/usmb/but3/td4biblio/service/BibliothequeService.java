package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.BibliothequeMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * La couche Service où se trouve toute la logique métier des bibliothèques.
 * Elle interagit avec la couche Repository pour accéder aux données.
 * Gère les bibliothèques et leur relation avec les adresses.
 * L'accès à certaines opérations est restreint aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
 */
@Service
@Transactional
public class BibliothequeService
        extends AbstractGenericService<Bibliotheque, Integer, BibliothequeResponseDto, BibliothequeDetailResponseDto, BibliothequeCreateDto> {

    private final AdresseRepo adresseRepo;

    /**
     * Constructeur du service Bibliothèque.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des bibliothèques
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param adresseRepo - repository des adresses
     */
    public BibliothequeService (BibliothequeRepo repository, BibliothequeMapper mapper, AdresseRepo adresseRepo) {
        super(repository, mapper);
        this.adresseRepo = adresseRepo;
    }

    /**
     * Récupère une bibliothèque spécifique par son identifiant avec toutes ses informations détaillées.
     * @param id - identifiant de la bibliothèque à récupérer
     * @return le DTO détaillé de la bibliothèque
     * @throws RessourceNotFoundException si la bibliothèque n'existe pas
     */
    @Override
    @Transactional
    public BibliothequeDetailResponseDto getById(Integer id){
        return super.getById(id);
    }

    /**
     * Met à jour une bibliothèque existante à partir des données du DTO.
     * Résout également la relation avec l'adresse.
     * Opération réservée aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
     * @param id - identifiant de la bibliothèque à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé de la bibliothèque mise à jour
     * @throws RessourceNotFoundException si la bibliothèque ou l'adresse n'existe pas
     */
    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public BibliothequeDetailResponseDto update(Integer id, BibliothequeCreateDto dto) {
        Bibliotheque bibliotheque = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvée : " + id));

        bibliotheque.setNom(dto.getNom());
        bibliotheque.setAdresse(adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvée : " + dto.getAdresseId())));

        return mapper.toDetailResponse(repository.save(bibliotheque));
    }

    /**
     * Crée une nouvelle bibliothèque à partir des données du DTO.
     * Résout également la relation avec l'adresse.
     * Opération réservée aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
     * @param dto - données de création de la bibliothèque
     * @return le DTO détaillé de la bibliothèque créée
     * @throws RessourceNotFoundException si l'adresse spécifiée n'existe pas
     */
    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public BibliothequeDetailResponseDto create(BibliothequeCreateDto dto) {
        Bibliotheque bibliotheque = mapper.toEntity(dto);
        bibliotheque.setAdresse(adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvée : " + dto.getAdresseId())));
        return mapper.toDetailResponse(repository.save(bibliotheque));
    }

    /**
     * Supprime une bibliothèque par son identifiant.
     * Opération réservée aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
     * @param id - identifiant de la bibliothèque à supprimer
     * @throws RessourceNotFoundException si la bibliothèque n'existe pas
     */
    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public void delete(Integer id) {
        super.delete(id);
    }

    /**
     * Récupère une liste paginée de bibliothèques filtrées par un terme de recherche sur le nom.
     * La recherche est insensible à la casse.
     * @param searchTerm - terme de recherche à appliquer sur le nom de la bibliothèque
     * @param pageable - paramètres de pagination (page, taille, tri)
     * @return page de DTOs de réponse correspondant aux critères
     */
    public Page<BibliothequeResponseDto> findPaginated(String searchTerm, Pageable pageable) {
        return ((BibliothequeRepo) repository)
                .findByNomContainingIgnoreCase(searchTerm, pageable)
                .map(mapper::toResponse);
    }
}