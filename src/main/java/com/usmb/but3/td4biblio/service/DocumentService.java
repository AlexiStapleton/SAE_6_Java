package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.DocumentMapper;
import com.usmb.but3.td4biblio.repository.DocumentRepo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DocumentService
        extends AbstractGenericService<
            Document,
            Integer,
            DocumentResponseDto,
            DocumentDetailResponseDto,
            DocumentCreateDto> {

    private final DocumentRepo documentRepo;

    public DocumentService(DocumentRepo repository, DocumentMapper mapper) {
        super(repository, mapper);
        this.documentRepo = repository;
    }

    // -------------------------------------------------------------------------
    // Fonctionnalités actives : getAll(), getById(), searchByTitre()
    // getAll() et getById() sont héritées de AbstractGenericService
    // -------------------------------------------------------------------------

    /**
     * Recherche des documents dont le titre contient la chaîne donnée (insensible à la casse).
     */
    public List<DocumentResponseDto> searchByTitre(String titre) {
        return documentRepo
                .findByTitreContainingIgnoreCase(titre)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // -------------------------------------------------------------------------
    // update() obligatoire (méthode abstraite) — non exposé en vue pour l'instant
    // -------------------------------------------------------------------------

    @Override
    public DocumentDetailResponseDto update(Integer id, DocumentCreateDto dto) {
        Document document = documentRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + id));

        document.setTitre(dto.getTitre());
        document.setGif(dto.getGif());
        document.setFormat(dto.getFormat());
        document.setDescription(dto.getDescription());
        document.setDateAcquisition(dto.getDateAcquisition());
        document.setDatePublication(dto.getDatePublication());
        document.setCodeEmplacement(dto.getCodeEmplacement());
        document.setEmpruntable(dto.getEmpruntable());

       return mapper.toDetailResponse(
        documentRepo.save(document)
        );
    }
}