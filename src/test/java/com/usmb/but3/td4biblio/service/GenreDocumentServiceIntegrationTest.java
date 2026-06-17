package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.GenreDocumentCreateDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link GenreDocumentService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GenreDocumentServiceIntegrationTest {

    @Autowired
    private GenreDocumentService genreDocumentService;

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllGenres() {
        List<GenreDocumentResponseDto> result = genreDocumentService.getAll();
        // data.sql insère 1 genre
        assertThat(result).hasSize(1);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnGenre_whenExists() {
        GenreDocumentResponseDto result = genreDocumentService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Roman");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> genreDocumentService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    void create_shouldPersistGenre() {
        GenreDocumentCreateDto dto = new GenreDocumentCreateDto();
        dto.setNom("Policier");

        GenreDocumentResponseDto result = genreDocumentService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNom()).isEqualTo("Policier");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldModifyGenre() {
        GenreDocumentCreateDto dto = new GenreDocumentCreateDto();
        dto.setNom("Roman Historique");

        GenreDocumentResponseDto result = genreDocumentService.update(1, dto);

        assertThat(result.getNom()).isEqualTo("Roman Historique");
    }

    @Test
    void update_shouldThrow_whenGenreNotFound() {
        GenreDocumentCreateDto dto = new GenreDocumentCreateDto();
        dto.setNom("Inconnu");

        assertThatThrownBy(() -> genreDocumentService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_shouldRemoveGenre() {
        GenreDocumentCreateDto dto = new GenreDocumentCreateDto();
        dto.setNom("A Supprimer");

        GenreDocumentResponseDto created = genreDocumentService.create(dto);
        Integer id = created.getId();

        genreDocumentService.delete(id);

        assertThatThrownBy(() -> genreDocumentService.getById(id))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> genreDocumentService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }
}