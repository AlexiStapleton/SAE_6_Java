package com.usmb.but3.td4biblio.controller;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblio/document")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/")
    public ResponseEntity<List<Document>> getAllDocuments() {

        return ResponseEntity.ok(
                documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(
            @PathVariable Integer id) {

        Document document =
                documentService.getDocumentById(id);

        if(document == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.ok(document);
    }

    @PostMapping("/")
    public ResponseEntity<Document> saveDocument(
            @RequestBody Document document) {

        return ResponseEntity.ok(
                documentService.saveDocument(document));
    }

    @PutMapping("/")
    public ResponseEntity<Document> updateDocument(
            @RequestBody Document document) {

        return ResponseEntity.ok(
                documentService.updateDocument(document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable Integer id) {

        documentService.deleteDocument(id);

        return ResponseEntity.ok(
                "Document supprimé");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocument(
            @RequestParam String titre) {

        return ResponseEntity.ok(
                documentService.searchByTitre(titre));
    }

    @GetMapping("/auteur/{id}")
    public ResponseEntity<List<Document>> getByAuteur(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                documentService.getByAuteurId(id));
    }
}