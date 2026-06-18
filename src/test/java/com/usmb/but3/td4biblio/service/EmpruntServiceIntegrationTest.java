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
 * Données en base (V1__Init.sql) :
 *   - utilisateur 1 (alice@mail.com) : abonnement valide (futur)
 *   - utilisateur 2 (paul@mail.com)  : abonnement échu (NULL)
 *   - document 1 : empruntable=TRUE,  emprunté par utilisateur 1
 *   - document 2 : empruntable=TRUE,  libre
 *   - document 3 : empruntable=TRUE,  libre
 *   - document 4 : empruntable=FALSE, libre
 *   - emprunt existant : (utilisateur=1, document=1)
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmpruntServiceIntegrationTest {

    @Autowired
    private EmpruntService empruntService;

    private static final Emprunt.EmpruntId EXISTING_ID = new Emprunt.EmpruntId(1, 1);
    private static final Emprunt.EmpruntId UNKNOWN_ID  = new Emprunt.EmpruntId(9999, 9999);

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllEmprunts() {
        List<EmpruntResponseDto> result = empruntService.getAll();
        assertThat(result).hasSize(1);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnEmprunt_whenExists() {
        EmpruntDetailResponseDto result = empruntService.getById(EXISTING_ID);
        assertThat(result).isNotNull();
        assertThat(result.getDateCreation()).isNotNull();
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> empruntService.getById(UNKNOWN_ID))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    void create_shouldPersistEmprunt_andSetDateCreation() {
        // utilisateur 1 (abonnement valide) emprunte document 3 (libre, empruntable)
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(1);
        dto.setDocumentId(3);

        EmpruntDetailResponseDto result = empruntService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getDateCreation()).isEqualTo(LocalDate.now());
        assertThat(result.getDateFin()).isEqualTo(LocalDate.now().plusWeeks(5));
    }

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

    @Test
    void create_shouldThrow_whenDocumentNotEmpruntable() {
        // document 4 : empruntable=FALSE ; utilisateur 1 : abonnement valide
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(1);
        dto.setDocumentId(4);

        assertThatThrownBy(() -> empruntService.create(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empruntable");
    }

    @Test
    void create_shouldThrow_whenDocumentAlreadyBorrowed() {
        // document 1 déjà emprunté (utilisateur 1) — un 2e emprunt dessus doit échouer
        // Note : utilisateur 1 ne peut pas re-emprunter (clé composite unique),
        // on passe par un utilisateur fictif valide si besoin ; ici le count suffit
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(1);
        dto.setDocumentId(1); // déjà en base → count > 0

        assertThatThrownBy(() -> empruntService.create(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("déjà emprunté");
    }

    @Test
    void create_shouldThrow_whenAbonnementEchu() {
        // utilisateur 2 : date_fin_abonnement=NULL → échu ; document 3 : libre
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(2);
        dto.setDocumentId(3);

        assertThatThrownBy(() -> empruntService.create(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("abonnement");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldSetProlongation_basedOnCurrentDateFin() {
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(1);
        dto.setDocumentId(1);

        EmpruntDetailResponseDto before = empruntService.getById(EXISTING_ID);
        LocalDate expectedProlongation = before.getDateFin().plusWeeks(5);

        EmpruntDetailResponseDto result = empruntService.update(EXISTING_ID, dto);

        assertThat(result.getProlongation()).isEqualTo(expectedProlongation);
    }

    @Test
    void update_shouldThrow_whenEmpruntNotFound() {
        EmpruntCreateDto dto = new EmpruntCreateDto();
        dto.setUtilisateurId(9999);
        dto.setDocumentId(9999);

        assertThatThrownBy(() -> empruntService.update(UNKNOWN_ID, dto))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_shouldRemoveEmprunt() {
        empruntService.delete(EXISTING_ID);

        assertThatThrownBy(() -> empruntService.getById(EXISTING_ID))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> empruntService.delete(UNKNOWN_ID))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── getAllByUtilisateurEmail ───────────────────────────────────────────────

    @Test
    void getAllByUtilisateurEmail_shouldReturnEmprunts_whenEmailExists() {
        // alice@mail.com = utilisateur 1, qui a 1 emprunt en base
        List<EmpruntResponseDto> result = empruntService.getAllByUtilisateurEmail("alice@mail.com");

        assertThat(result).isNotEmpty();
    }

    @Test
    void getAllByUtilisateurEmail_shouldReturnEmpty_whenEmailUnknown() {
        List<EmpruntResponseDto> result = empruntService.getAllByUtilisateurEmail("inconnu@nowhere.com");

        assertThat(result).isEmpty();
    }

    // ─── getDocumentIdsActuellementEmpruntes ───────────────────────────────────

    @Test
    void getDocumentIdsActuellementEmpruntes_shouldContainBorrowedDocument() {
        List<Integer> ids = empruntService.getDocumentIdsActuellementEmpruntes();

        assertThat(ids).contains(1);
    }

    // ─── getUtilisateurIdsAyantAtteintLimite ──────────────────────────────────

    @Test
    void getUtilisateurIdsAyantAtteintLimite_shouldReturnEmpty_byDefault() {
        List<Integer> ids = empruntService.getUtilisateurIdsAyantAtteintLimite();

        assertThat(ids).isEmpty();
    }
}