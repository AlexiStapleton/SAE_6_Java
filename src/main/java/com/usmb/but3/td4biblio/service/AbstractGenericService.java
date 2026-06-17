package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.GenericMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Service générique abstrait contenant la logique métier commune à tous les services.
 * Cette classe fournit les opérations CRUD de base pour toutes les entités.
 * Elle interagit avec la couche Repository pour accéder aux données et utilise les mappers pour la conversion.
 *
 * @param <T> - type de l'entité
 * @param <ID> - type de l'identifiant de l'entité
 * @param <RespDto> - type du DTO de réponse
 * @param <DetailDto> - type du DTO détaillé
 * @param <CreateDto> - type du DTO de création/modification
 */
@RequiredArgsConstructor
@Slf4j
@Transactional
public abstract class AbstractGenericService<T, ID, RespDto, DetailDto, CreateDto>
        implements GenericService<T, ID, RespDto, DetailDto ,CreateDto> {

    protected final JpaRepository<T, ID> repository;
    protected final GenericMapper<T, RespDto, DetailDto, CreateDto> mapper;

    /**
     * Récupère la liste complète de toutes les entités de ce type.
     * @return liste de tous les DTOs de réponse
     */
    @Override
    public List<RespDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Récupère une entité spécifique par son identifiant.
     * @param id - identifiant de l'entité à récupérer
     * @return le DTO détaillé de l'entité
     * @throws RessourceNotFoundException si l'entité n'existe pas
     */
    @Override
    public DetailDto getById(ID id) {
        return mapper.toDetailResponse(
                repository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id))
        );
    }

    /**
     * Crée une nouvelle entité à partir des données du DTO.
     * @param dto - données de création de l'entité
     * @return le DTO détaillé de l'entité créée
     */
    @Override
    public DetailDto create(CreateDto dto) {
        return mapper.toDetailResponse(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Supprime une entité par son identifiant.
     * @param id - identifiant de l'entité à supprimer
     * @throws RessourceNotFoundException si l'entité n'existe pas
     */
    @Override
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new RessourceNotFoundException("Entité non trouvée : " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Met à jour une entité existante à partir des données du DTO.
     * Cette méthode doit être implémentée par les classes concrètes.
     * @param id - identifiant de l'entité à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé de l'entité mise à jour
     */
    public abstract DetailDto update(ID id, CreateDto dto);
}