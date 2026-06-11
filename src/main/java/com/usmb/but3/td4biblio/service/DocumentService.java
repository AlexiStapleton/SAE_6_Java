package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.repository.DocumentRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepo documentRepo;

    public List<Document> getAllDocuments() {
        return documentRepo.findAll(
                Sort.by(Sort.Direction.ASC, "id"));
    }

    public Document getDocumentById(Integer id) {

        Optional<Document> optionalDocument =
                documentRepo.findById(id);

        if(optionalDocument.isPresent()) {
            return optionalDocument.get();
        }

        log.error("Document {} introuvable", id);
        return null;
    }

    public Document saveDocument(Document document) {

        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        return documentRepo.save(document);
    }

    public Document updateDocument(Document document) {

        Optional<Document> existing =
                documentRepo.findById(document.getId());

        if(existing.isEmpty()) {
            return null;
        }

        document.setCreatedAt(
                existing.get().getCreatedAt());

        document.setUpdatedAt(
                LocalDateTime.now());

        return documentRepo.save(document);
    }

    public void deleteDocument(Integer id) {

        documentRepo.deleteById(id);

        log.info("Document {} supprimé", id);
    }

    public List<Document> getByAuteurId(Integer auteurId) {
        return documentRepo.findByAuteurId(
                auteurId,
                Sort.by("id"));
    }

    public List<Document> searchByTitre(String titre) {
        return documentRepo.findByTitreContainingIgnoreCase(titre);
    }
}