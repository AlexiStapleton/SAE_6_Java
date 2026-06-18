package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.CodeRaisonCreateDto;
import com.usmb.but3.td4biblio.dto.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link CodeRaisonService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CodeRaisonServiceIntegrationTest {

    @Autowired
    private CodeRaisonService codeRaisonService;

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllCodesRaison() {
        List<CodeRaisonResponseDto> result = codeRaisonService.getAll();
        // data.sql insère 1 code raison
        assertThat(result).hasSize(1);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnCodeRaison_whenExists() {
        CodeRaisonResponseDto result = codeRaisonService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("ACHAT");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> codeRaisonService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    void create_shouldPersistCodeRaison() {
        CodeRaisonCreateDto dto = new CodeRaisonCreateDto();
        dto.setNom("DON");
        dto.setDescription("Document reçu en don");

        CodeRaisonResponseDto result = codeRaisonService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNom()).isEqualTo("DON");
        assertThat(result.getDescription()).isEqualTo("Document reçu en don");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldModifyCodeRaison() {
        CodeRaisonCreateDto dto = new CodeRaisonCreateDto();
        dto.setNom("ACHAT_MODIFIE");
        dto.setDescription("Nouvelle description");

        CodeRaisonResponseDto result = codeRaisonService.update(1, dto);

        assertThat(result.getNom()).isEqualTo("ACHAT_MODIFIE");
        assertThat(result.getDescription()).isEqualTo("Nouvelle description");
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        CodeRaisonCreateDto dto = new CodeRaisonCreateDto();
        dto.setNom("INCONNU");

        assertThatThrownBy(() -> codeRaisonService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_shouldRemoveCodeRaison() {
        CodeRaisonCreateDto dto = new CodeRaisonCreateDto();
        dto.setNom("SUPPRESSION");
        dto.setDescription("À supprimer");

        CodeRaisonResponseDto created = codeRaisonService.create(dto);
        Integer id = created.getId();

        codeRaisonService.delete(id);

        assertThatThrownBy(() -> codeRaisonService.getById(id))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> codeRaisonService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }
}