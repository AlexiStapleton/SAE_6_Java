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

@Transactional
public abstract class AbstractDocumentService<T extends Document, RespDto extends DocumentResponseDto, DetailDto extends DocumentDetailResponseDto, CreateDto extends DocumentCreateDto>
        extends AbstractGenericService<T, Integer, RespDto, DetailDto, CreateDto>{

    private final AuteurRepo auteurRepository;
    private final EditeurRepo editeurRepository;
    private final BibliothequeRepo bibliothequeRepository;
    private final GenreDocumentRepo genreRepository;
    private final CodeRaisonRepo codeRaisonRepository;
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