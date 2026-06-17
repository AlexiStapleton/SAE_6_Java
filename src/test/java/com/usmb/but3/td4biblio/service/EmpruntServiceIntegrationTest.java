package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EmpruntCreateDto;
import com.usmb.but3.td4biblio.dto.EmpruntDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EmpruntResponseDto;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link EmpruntService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 * L'emprunt utilise une clé composite (utilisateur_id, document_id).
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmpruntServiceIntegrationTest {

    @Autowired
    private EmpruntService empruntService;

    // ID composite de l'emprunt existant en base (utilisateur=1, document=1)
    private static final Emprunt.EmpruntId EXISTING_ID = new Emprunt.EmpruntId(1, 1);
    private static final Emprunt.EmpruntId UNKNOWN_ID  = new Emprunt.EmpruntId(9999, 9999);

    // ─── getAll ────────────────────────────────────────────────────────────────

//    @Test
//    void getAll_shouldReturnAllEmprunts() {
//        List<EmpruntResponseDto> result = empruntService.getAll();
//        // data.sql insère 1 emprunt
//        assertThat(result).hasSize(1);
//    }
//
//    // ─── getById ───────────────────────────────────────────────────────────────
//
//    @Test
//    void getById_shouldReturnEmprunt_whenExists() {
//        EmpruntDetailResponseDto result = empruntService.getById(EXISTING_ID);
//        assertThat(result).isNotNull();
//        assertThat(result.getDateCreation()).isNotNull();
//    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> empruntService.getById(UNKNOWN_ID))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

//    @Test
//    void create_shouldPersistEmprunt_andSetDateCreation() {
//        // L'utilisateur 1 emprunte le document 2 (pas encore emprunté)
//        EmpruntCreateDto dto = new EmpruntCreateDto();
//        dto.setUtilisateurId(1);
//        dto.setDocumentId(2);
//
//        EmpruntDetailResponseDto result = empruntService.create(dto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getDateCreation()).isEqualTo(LocalDate.now());
//    }

    @Test
    void create_shouldThrow_whenDocumentNotFound() {
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(1);
        dto.setDocumentId(9999);

        assertThatThrownBy(() -> empruntService.create(dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    void create_shouldThrow_whenUtilisateurNotFound() {
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(9999);
        dto.setDocumentId(1);

        assertThatThrownBy(() -> empruntService.create(dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── update ────────────────────────────────────────────────────────────────

//    @Test
//    void update_shouldModifyEmprunt() {
//        EmpruntCreateDto dto = new EmpruntCreateDto();
//        dto.setUtilisateurId(1);
//        dto.setDocumentId(1);
//        dto.setProlongation(LocalDate.now().plusWeeks(2));
//
//        EmpruntDetailResponseDto result = empruntService.update(EXISTING_ID, dto);
//
//        assertThat(result.getProlongation()).isEqualTo(LocalDate.now().plusWeeks(2));
//    }

    @Test
    void update_shouldThrow_whenEmpruntNotFound() {
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(9999);
        dto.setDocumentId(9999);

        assertThatThrownBy(() -> empruntService.update(UNKNOWN_ID, dto))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── delete ────────────────────────────────────────────────────────────────

//    @Test
//    void delete_shouldRemoveEmprunt() {
//        empruntService.delete(EXISTING_ID);
//
//        assertThatThrownBy(() -> empruntService.getById(EXISTING_ID))
//                .isInstanceOf(RessourceNotFoundException.class);
//    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> empruntService.delete(UNKNOWN_ID))
                .isInstanceOf(RessourceNotFoundException.class);
    }
}