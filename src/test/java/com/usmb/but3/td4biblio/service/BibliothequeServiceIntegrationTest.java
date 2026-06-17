package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link BibliothequeService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BibliothequeServiceIntegrationTest {

    @Autowired
    private BibliothequeService bibliothequeService;

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllLibraries() {
        List<BibliothequeResponseDto> result = bibliothequeService.getAll();
        // data.sql insère 1 bibliothèque
        assertThat(result).hasSize(1);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnLibrary_whenExists() {
        BibliothequeDetailResponseDto result = bibliothequeService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Biblio Chambéry");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> bibliothequeService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void create_shouldPersistLibrary() {
        BibliothequeCreateDto dto = new BibliothequeCreateDto();
        dto.setNom("Biblio Grenoble");
        dto.setAdresseId(2);

        BibliothequeDetailResponseDto result = bibliothequeService.create(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getNom()).isEqualTo("Biblio Grenoble");
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void create_shouldThrow_whenAdresseNotFound() {
        BibliothequeCreateDto dto = new BibliothequeCreateDto();
        dto.setNom("Biblio Inconnue");
        dto.setAdresseId(9999);

        assertThatThrownBy(() -> bibliothequeService.create(dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void update_shouldModifyLibrary() {
        BibliothequeCreateDto dto = new BibliothequeCreateDto();
        dto.setNom("Biblio Chambéry Modifiée");
        dto.setAdresseId(1);

        BibliothequeDetailResponseDto result = bibliothequeService.update(1, dto);

        assertThat(result.getNom()).isEqualTo("Biblio Chambéry Modifiée");
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void update_shouldThrow_whenLibraryNotFound() {
        BibliothequeCreateDto dto = new BibliothequeCreateDto();
        dto.setNom("Inconnu");
        dto.setAdresseId(1);

        assertThatThrownBy(() -> bibliothequeService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void update_shouldThrow_whenAdresseNotFound() {
        BibliothequeCreateDto dto = new BibliothequeCreateDto();
        dto.setNom("Biblio Chambéry");
        dto.setAdresseId(9999);

        assertThatThrownBy(() -> bibliothequeService.update(1, dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BIBLIOTHECAIRE")
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> bibliothequeService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── findPaginated ─────────────────────────────────────────────────────────

    @Test
    void findPaginated_shouldReturnResults_whenTermMatches() {
        Page<BibliothequeResponseDto> result = bibliothequeService.findPaginated(
                "Chambéry", PageRequest.of(0, 10)
        );
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("Biblio Chambéry");
    }

    @Test
    void findPaginated_shouldReturnEmpty_whenTermDoesNotMatch() {
        Page<BibliothequeResponseDto> result = bibliothequeService.findPaginated(
                "Inconnu", PageRequest.of(0, 10)
        );
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    void findPaginated_shouldBeCaseInsensitive() {
        Page<BibliothequeResponseDto> result = bibliothequeService.findPaginated(
                "chambéry", PageRequest.of(0, 10)
        );
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findPaginated_shouldRespectPageSize() {
        Page<BibliothequeResponseDto> result = bibliothequeService.findPaginated(
                "", PageRequest.of(0, 1)
        );
        assertThat(result.getContent()).hasSize(1);
    }
}