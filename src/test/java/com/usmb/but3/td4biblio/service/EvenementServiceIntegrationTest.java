package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.dto.EvenementDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EvenementResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link EvenementService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 * Les opérations d'écriture sont protégées par le rôle BIBLIOTHECAIRE.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EvenementServiceIntegrationTest {

    @Autowired
    private EvenementService evenementService;

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllEvenements() {
        List<EvenementResponseDto> result = evenementService.getAll();
        // data.sql insère 1 événement
        assertThat(result).hasSize(1);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnEvenement_whenExists() {
        EvenementDetailResponseDto result = evenementService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Event test");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> evenementService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void create_shouldPersistEvenement() {
        EvenementCreateDto dto = new EvenementCreateDto();
        dto.setNom("Atelier BD");
        dto.setBibliothequeId(1);
        dto.setTypeEvenementId(1);
        dto.setDateDebut(LocalDate.now());
        dto.setDateFin(LocalDate.now().plusDays(1));

        EvenementDetailResponseDto result = evenementService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNom()).isEqualTo("Atelier BD");
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void create_shouldThrow_whenBibliothequeNotFound() {
        EvenementCreateDto dto = new EvenementCreateDto();
        dto.setNom("Atelier BD");
        dto.setBibliothequeId(9999);
        dto.setTypeEvenementId(1);

        assertThatThrownBy(() -> evenementService.create(dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void create_shouldThrow_whenTypeEvenementNotFound() {
        EvenementCreateDto dto = new EvenementCreateDto();
        dto.setNom("Atelier BD");
        dto.setBibliothequeId(1);
        dto.setTypeEvenementId(9999);

        assertThatThrownBy(() -> evenementService.create(dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void update_shouldModifyEvenement() {
        EvenementCreateDto dto = new EvenementCreateDto();
        dto.setNom("Event modifié");
        dto.setBibliothequeId(1);
        dto.setTypeEvenementId(1);

        EvenementDetailResponseDto result = evenementService.update(1, dto);

        assertThat(result.getNom()).isEqualTo("Event modifié");
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void update_shouldThrow_whenEvenementNotFound() {
        EvenementCreateDto dto = new EvenementCreateDto();
        dto.setNom("Inconnu");
        dto.setBibliothequeId(1);
        dto.setTypeEvenementId(1);

        assertThatThrownBy(() -> evenementService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void delete_shouldRemoveEvenement() {
        // Créer un événement à supprimer pour éviter les FK des données fixes
        EvenementCreateDto dto = new EvenementCreateDto();
        dto.setNom("A supprimer");
        dto.setBibliothequeId(1);
        dto.setTypeEvenementId(1);
        dto.setDateDebut(LocalDate.now());
        dto.setDateFin(LocalDate.now().plusDays(1));

        EvenementDetailResponseDto created = evenementService.create(dto);
        Integer id = created.getId();

        evenementService.delete(id);

        assertThatThrownBy(() -> evenementService.getById(id))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> evenementService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── findPaginated ─────────────────────────────────────────────────────────

    @Test
    void findPaginated_shouldReturnResults_whenTermMatches() {
        Page<EvenementResponseDto> result = evenementService.findPaginated(
                "Event", PageRequest.of(0, 10)
        );
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findPaginated_shouldReturnEmpty_whenTermDoesNotMatch() {
        Page<EvenementResponseDto> result = evenementService.findPaginated(
                "Inconnu", PageRequest.of(0, 10)
        );
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    void findPaginated_shouldBeCaseInsensitive() {
        Page<EvenementResponseDto> result = evenementService.findPaginated(
                "event", PageRequest.of(0, 10)
        );
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}