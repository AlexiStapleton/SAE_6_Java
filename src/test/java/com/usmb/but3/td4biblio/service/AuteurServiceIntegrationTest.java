package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.AuteurCreateDto;
import com.usmb.but3.td4biblio.dto.AuteurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link AuteurService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuteurServiceIntegrationTest {

    @Autowired
    private AuteurService auteurService;

    // ─── getAll ────────────────────────────────────────────────────────────────
    @Test
    void getAll_shouldReturnAllAuthors() {
        List<AuteurResponseDto> result = auteurService.getAll();
        // data.sql insère 2 auteurs
        assertThat(result).hasSize(2);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnAuthor_whenExists() {
        AuteurDetailResponseDto result = auteurService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Hugo");
        assertThat(result.getPrenom()).isEqualTo("Victor");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> auteurService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    void create_shouldPersistAuthor() {
        AuteurCreateDto dto = new AuteurCreateDto();
        dto.setNom("Zola");
        dto.setPrenom("Émile");
        dto.setNationalite("FR");
        dto.setTypesAuteurIds(List.of(1));

        AuteurDetailResponseDto result = auteurService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNom()).isEqualTo("Zola");
        assertThat(result.getPrenom()).isEqualTo("Émile");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldModifyAuthor() {
        AuteurCreateDto dto = new AuteurCreateDto();
        dto.setNom("Hugo");
        dto.setPrenom("Victor-Marie");
        dto.setNationalite("FR");
        dto.setTypesAuteurIds(List.of(1));

        AuteurDetailResponseDto result = auteurService.update(1, dto);

        assertThat(result.getPrenom()).isEqualTo("Victor-Marie");
    }

    @Test
    void update_shouldThrow_whenAuthorNotFound() {
        AuteurCreateDto dto = new AuteurCreateDto();
        dto.setNom("Inconnu");
        dto.setPrenom("X");
        dto.setTypesAuteurIds(List.of(1));

        assertThatThrownBy(() -> auteurService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_shouldRemoveAuthor() {
        // On crée d'abord un auteur sans relations pour pouvoir le supprimer
        AuteurCreateDto dto = new AuteurCreateDto();
        dto.setNom("Flaubert");
        dto.setPrenom("Gustave");
        dto.setNationalite("FR");
        dto.setTypesAuteurIds(List.of());

        AuteurDetailResponseDto created = auteurService.create(dto);
        Integer id = created.getId();

        auteurService.delete(id);

        assertThatThrownBy(() -> auteurService.getById(id))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenAuthorNotFound() {
        assertThatThrownBy(() -> auteurService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── recherches métier ─────────────────────────────────────────────────────

    @Test
    void getAuteursByNom_shouldReturnMatches() {
        List<AuteurResponseDto> result = auteurService.getAuteursByNom("Hugo");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Hugo");
    }

    @Test
    void getAuteursByNom_shouldReturnEmpty_whenNoMatch() {
        List<AuteurResponseDto> result = auteurService.getAuteursByNom("Inconnu");
        assertThat(result).isEmpty();
    }

    @Test
    void getAuteursByNomAndPrenom_shouldReturnMatch() {
        List<AuteurResponseDto> result = auteurService.getAuteursByNomAndPrenom("Hugo", "Victor");
        assertThat(result).hasSize(1);
    }

    @Test
    void getAuteursByNomAndPrenom_shouldReturnEmpty_whenPrenomDiffers() {
        List<AuteurResponseDto> result = auteurService.getAuteursByNomAndPrenom("Hugo", "Paul");
        assertThat(result).isEmpty();
    }

    @Test
    void getAuteursByNomStartWithIgnoreCase_shouldReturnMatches() {
        List<AuteurResponseDto> result = auteurService.getAuteursByNomStartWithIgnoreCase("hug");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Hugo");
    }

    @Test
    void getByNomContainingIgnoreCase_shouldReturnMatches() {
        List<AuteurResponseDto> result = auteurService.getByNomContainingIgnoreCase("org");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Borges");
    }

    @Test
    void getByNomContainingIgnoreCase_shouldReturnEmpty_whenNoMatch() {
        List<AuteurResponseDto> result = auteurService.getByNomContainingIgnoreCase("zzz");
        assertThat(result).isEmpty();
    }
}