package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EditeurCreateDto;
import com.usmb.but3.td4biblio.dto.EditeurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link EditeurService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EditeurServiceIntegrationTest {

    @Autowired
    private EditeurService editeurService;

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllEditeurs() {
        List<EditeurResponseDto> result = editeurService.getAll();
        // data.sql insère 2 éditeurs
        assertThat(result).hasSize(2);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnEditeur_whenExists() {
        EditeurDetailResponseDto result = editeurService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Garnier");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> editeurService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    void create_shouldPersistEditeur() {
        EditeurCreateDto dto = new EditeurCreateDto();
        dto.setNom("Gallimard");
        dto.setAdresseId(1);

        EditeurDetailResponseDto result = editeurService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNom()).isEqualTo("Gallimard");
    }

    @Test
    void create_shouldThrow_whenAdresseNotFound() {
        EditeurCreateDto dto = new EditeurCreateDto();
        dto.setNom("Flammarion");
        dto.setAdresseId(9999);

        assertThatThrownBy(() -> editeurService.create(dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldModifyEditeur() {
        EditeurCreateDto dto = new EditeurCreateDto();
        dto.setNom("Garnier Modifié");
        dto.setAdresseId(1);

        EditeurDetailResponseDto result = editeurService.update(1, dto);

        assertThat(result.getNom()).isEqualTo("Garnier Modifié");
    }

    @Test
    void update_shouldThrow_whenEditeurNotFound() {
        EditeurCreateDto dto = new EditeurCreateDto();
        dto.setNom("Inconnu");
        dto.setAdresseId(1);

        assertThatThrownBy(() -> editeurService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void update_shouldThrow_whenAdresseNotFound() {
        EditeurCreateDto dto = new EditeurCreateDto();
        dto.setNom("Garnier");
        dto.setAdresseId(9999);

        assertThatThrownBy(() -> editeurService.update(1, dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_shouldRemoveEditeur() {
        EditeurCreateDto dto = new EditeurCreateDto();
        dto.setNom("A Supprimer");
        dto.setAdresseId(1);

        EditeurDetailResponseDto created = editeurService.create(dto);
        Integer id = created.getId();

        editeurService.delete(id);

        assertThatThrownBy(() -> editeurService.getById(id))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> editeurService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }
}