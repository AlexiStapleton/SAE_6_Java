package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Dvd;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.DocumentMapper;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.DvdRepo;
import com.usmb.but3.td4biblio.repository.LivreRepo;

import com.usmb.but3.td4biblio.enumeration.ChampRechercheDocument;
import com.usmb.but3.td4biblio.enumeration.TypeRecherche;
import com.usmb.but3.td4biblio.enumeration.TypeDocument;

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
    private final LivreRepo livreRepo;
    private final DvdRepo dvdRepo;

    public DocumentService(DocumentRepo repository, DocumentMapper mapper, LivreRepo livreRepo, DvdRepo dvdRepo) {
        super(repository, mapper);
        this.documentRepo = repository;
        this.livreRepo = livreRepo;
        this.dvdRepo = dvdRepo;
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

    @Override
    public DocumentDetailResponseDto getById(Integer id) {

        DocumentDetailResponseDto dto = super.getById(id);

        if (livreRepo.existsById(id)) {
            dto.setType(TypeDocument.LIVRE);
        } else if (dvdRepo.existsById(id)) {
            dto.setType(TypeDocument.DVD);
        }

        return dto;
    }

    public Document getEntityById(Integer id) {
        return documentRepo.findById(id)
                .orElseThrow(() ->
                        new RessourceNotFoundException(
                                "Document non trouvé : " + id
                        ));
    }

    /**
     * Retourne tous les documents, filtrés optionnellement par type.
     * Si typeDoc est null, retourne tous les documents.
     */
    public List<DocumentResponseDto> getAll(TypeDocument typeDoc) {
        if (typeDoc == null) {
            return getAll();
        }
        if (typeDoc == TypeDocument.LIVRE) {
            return livreRepo.findAll()
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        } else {
            return dvdRepo.findAll()
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }
    }

    /*   Méthodes de recherche   */

    public List<DocumentResponseDto> searchDocuments(
            ChampRechercheDocument champ,
            TypeRecherche type,
            String valeur,
            TypeDocument typeDoc
    ) {
        List<Document> documents;

        switch (champ) {
            case TITRE      -> documents = searchTitre(type, valeur);
            case AUTEUR     -> documents = searchAuteur(type, valeur);
            case EDITEUR    -> documents = searchEditeur(type, valeur);
            case BIBLIOTHEQUE -> documents = searchBibliotheque(type, valeur);
            case GENRE      -> documents = searchGenre(type, valeur);
            default         -> documents = List.of();
        }

        // Filtrage par type si demandé
        if (typeDoc != null) {
            documents = documents.stream()
                    .filter(doc -> {
                        if (typeDoc == TypeDocument.LIVRE) {
                            return livreRepo.existsById(doc.getId());
                        } else {
                            return dvdRepo.existsById(doc.getId());
                        }
                    })
                    .toList();
        }

        return documents.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /** Surcharge de compatibilité (sans filtre type) */
    public List<DocumentResponseDto> searchDocuments(
            ChampRechercheDocument champ,
            TypeRecherche type,
            String valeur
    ) {
        return searchDocuments(champ, type, valeur, null);
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