package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.dto.EvenementDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EvenementResponseDto;
import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EvenementMapper;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;
import com.usmb.but3.td4biblio.repository.EvenementRepo;
import com.usmb.but3.td4biblio.repository.TypeEvenementRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


/**
 * La couche Service où se trouve toute la logique métier des événements.
 * Elle interagit avec la couche Repository pour accéder aux données.
 * Gère les événements et leur relation avec les bibliothèques et les types d'événements.
 * L'accès à certaines opérations est restreint aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
 */
@Service
@Transactional
public class EvenementService extends AbstractGenericService<Evenement, Integer, EvenementResponseDto, EvenementDetailResponseDto, EvenementCreateDto>{

    private final BibliothequeRepo bibliothequeRepo;
    private final TypeEvenementRepo typeEvenementRepo;

    /**
     * Constructeur du service Événement.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des événements
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param bibliothequeRepo - repository des bibliothèques
     * @param typeEvenementRepo - repository des types d'événements
     */
    public EvenementService(EvenementRepo repository, EvenementMapper mapper, BibliothequeRepo bibliothequeRepo, TypeEvenementRepo typeEvenementRepo) {
        super(repository, mapper);
        this.bibliothequeRepo = bibliothequeRepo;
        this.typeEvenementRepo = typeEvenementRepo;
    }

    /**
     * Met à jour un événement existant à partir des données du DTO.
     * Résout également les relations avec la bibliothèque et le type d'événement.
     * Opération réservée aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
     * @param id - identifiant de l'événement à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé de l'événement mis à jour
     * @throws RessourceNotFoundException si l'événement, la bibliothèque ou le type d'événement n'existe pas
     */
    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public EvenementDetailResponseDto update(Integer id, EvenementCreateDto dto){
        Evenement evenement = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Évenement non trouvé : " + id));

        mapper.updateFromDto(dto, evenement);

        evenement.setBibliotheque(bibliothequeRepo.findById(dto.getBibliothequeId())
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvée : " + dto.getBibliothequeId())));

        evenement.setTypeEvenement(typeEvenementRepo.findById(dto.getTypeEvenementId())
                .orElseThrow(() -> new RessourceNotFoundException("Type évènement non trouvé : " + dto.getTypeEvenementId())));

        return mapper.toDetailResponse(repository.save(evenement));

    }

    /**
     * Crée un nouvel événement à partir des données du DTO.
     * Résout également les relations avec la bibliothèque et le type d'événement.
     * Opération réservée aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
     * @param dto - données de création de l'événement
     * @return le DTO détaillé de l'événement créé
     * @throws RessourceNotFoundException si la bibliothèque ou le type d'événement spécifiés n'existent pas
     */
    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public EvenementDetailResponseDto create(EvenementCreateDto dto) {
        Evenement evenement = mapper.toEntity(dto);

        evenement.setBibliotheque(bibliothequeRepo.findById(dto.getBibliothequeId())
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvée : " + dto.getBibliothequeId())));

        evenement.setTypeEvenement(typeEvenementRepo.findById(dto.getTypeEvenementId())
                .orElseThrow(() -> new RessourceNotFoundException("Type évènement non trouvé : " + dto.getTypeEvenementId())));

        return mapper.toDetailResponse(repository.save(evenement));
    }

    /**
     * Supprime un événement par son identifiant.
     * Opération réservée aux utilisateurs ayant le rôle BIBLIOTHECAIRE.
     * @param id - identifiant de l'événement à supprimer
     * @throws RessourceNotFoundException si l'événement n'existe pas
     */
    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public void delete(Integer id) {
        super.delete(id);
    }

    /**
     * Récupère une liste paginée d'événements filtrés par un terme de recherche sur le nom.
     * La recherche est insensible à la casse.
     * @param searchTerm - terme de recherche à appliquer sur le nom de l'événement
     * @param pageable - paramètres de pagination (page, taille, tri)
     * @return page de DTOs de réponse correspondant aux critères
     */
    public Page<EvenementResponseDto> findPaginated(String searchTerm, Pageable pageable) {
        return ((EvenementRepo) repository)
                .findByNomContainingIgnoreCase(searchTerm, pageable)
                .map(mapper::toResponse);
    }

    /**
     * Retourne les 5 prochains événements à venir (date de début >= aujourd'hui),
     * triés par date de début croissante. Utilisé par la page d'accueil.
     * @return liste des prochains événements
     */
    public List<EvenementResponseDto> getUpcomingEvenements() {
        return ((EvenementRepo) repository)
                .findTop5ByDateDebutGreaterThanEqualOrderByDateDebutAsc(LocalDate.now())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}