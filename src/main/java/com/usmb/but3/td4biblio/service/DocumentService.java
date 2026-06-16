package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.DocumentMapper;
import com.usmb.but3.td4biblio.repository.DocumentRepo;

import com.usmb.but3.td4biblio.enumeration.ChampRechercheDocument;
import com.usmb.but3.td4biblio.enumeration.TypeRecherche;

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

    /*   Méthodes de recherche   */

    public List<DocumentResponseDto> searchDocuments(
            ChampRechercheDocument champ,
            TypeRecherche type,
            String valeur
    ) {

        List<Document> documents;

        switch (champ) {

            case TITRE -> documents = searchTitre(type, valeur);

            case AUTEUR -> documents = searchAuteur(type, valeur);

            case EDITEUR -> documents = searchEditeur(type, valeur);

            case BIBLIOTHEQUE -> documents = searchBibliotheque(type, valeur);

            case GENRE -> documents = searchGenre(type, valeur);

            default -> documents = List.of();
        }

        return documents.stream()
                .map(mapper::toResponse)
                .toList();
    }

    private List<Document> searchTitre(
            TypeRecherche type,
            String valeur
    ) {
        return switch (type) {

            case CONTIENT ->
                    documentRepo.findByTitreContainingIgnoreCase(valeur);

            case COMMENCE_PAR ->
                    documentRepo.findByTitreStartsWithIgnoreCase(valeur);

            case EGAL ->
                    documentRepo.findByTitreIgnoreCase(valeur);
        };
    }

    private List<Document> searchAuteur(
            TypeRecherche type,
            String valeur
    ) {
        return switch (type) {

            case CONTIENT ->
                    documentRepo.findByAuteurNomContainingIgnoreCase(valeur);

            case COMMENCE_PAR ->
                    documentRepo.findByAuteurNomStartsWithIgnoreCase(valeur);

            case EGAL ->
                    documentRepo.findByAuteurNomIgnoreCase(valeur);
        };
    }

    private List<Document> searchEditeur(
            TypeRecherche type,
            String valeur
    ) {
        return switch (type) {

            case CONTIENT ->
                    documentRepo.findByEditeurNomContainingIgnoreCase(valeur);

            case COMMENCE_PAR ->
                    documentRepo.findByEditeurNomStartsWithIgnoreCase(valeur);

            case EGAL ->
                    documentRepo.findByEditeurNomIgnoreCase(valeur);
        };
    }


    private List<Document> searchBibliotheque(
            TypeRecherche type,
            String valeur
    ) {
        return switch (type) {

            case CONTIENT ->
                    documentRepo.findByBibliothequeNomContainingIgnoreCase(valeur);

            case COMMENCE_PAR ->
                    documentRepo.findByBibliothequeNomStartsWithIgnoreCase(valeur);

            case EGAL ->
                    documentRepo.findByBibliothequeNomIgnoreCase(valeur);
        };
    }

    private List<Document> searchGenre(
            TypeRecherche type,
            String valeur
    ) {
        return switch (type) {

            case CONTIENT ->
                    documentRepo.findByGenreNomContainingIgnoreCase(valeur);

            case COMMENCE_PAR ->
                    documentRepo.findByGenreNomStartsWithIgnoreCase(valeur);

            case EGAL ->
                    documentRepo.findByGenreNomIgnoreCase(valeur);
        };
    }

}