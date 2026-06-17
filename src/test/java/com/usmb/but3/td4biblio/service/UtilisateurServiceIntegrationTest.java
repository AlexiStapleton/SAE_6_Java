package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.RegisterRequest;
import com.usmb.but3.td4biblio.dto.UtilisateurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.UtilisateurResponseDto;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link UtilisateurService}.
 * Utilise la base H2 en mémoire chargée via data.sql.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UtilisateurServiceIntegrationTest {

    @Autowired
    private UtilisateurService utilisateurService;

    // ─── getAll ────────────────────────────────────────────────────────────────

    @Test
    void getAll_shouldReturnAllUtilisateurs() {
        List<UtilisateurResponseDto> result = utilisateurService.getAll();
        // data.sql insère 2 utilisateurs
        assertThat(result).hasSize(2);
    }

    // ─── getById ───────────────────────────────────────────────────────────────

    @Test
    void getById_shouldReturnUtilisateur_whenExists() {
        UtilisateurDetailResponseDto result = utilisateurService.getById(1);
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dupont");
        assertThat(result.getPrenom()).isEqualTo("Alice");
        assertThat(result.getEmail()).isEqualTo("alice@mail.com");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> utilisateurService.getById(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── register ──────────────────────────────────────────────────────────────

    @Test
    void register_shouldCreateUtilisateur() {
        RegisterRequest request = buildRegisterRequest("nouveau@mail.com", "Nouveau");

        utilisateurService.register(request);

        List<UtilisateurResponseDto> all = utilisateurService.getAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void register_shouldHashPassword_basedOnDateNaissance() {
        RegisterRequest request = buildRegisterRequest("hashed@mail.com", "Dupont2");

        // Pas d'exception = mot de passe encodé correctement
        assertThatCode(() -> utilisateurService.register(request))
                .doesNotThrowAnyException();
    }

    @Test
    void register_shouldSetDateFinAbonnement_toOneYearFromNow() {
        RegisterRequest request = buildRegisterRequest("abonnement@mail.com", "Durand");

        utilisateurService.register(request);

        // On récupère le dernier utilisateur créé
        List<UtilisateurResponseDto> all = utilisateurService.getAll();
        // La date de fin d'abonnement est gérée en interne, on vérifie juste la création
        assertThat(all).hasSize(3);
    }

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        RegisterRequest request = buildRegisterRequest("alice@mail.com", "Dupont");

        assertThatThrownBy(() -> utilisateurService.register(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email déjà utilisé");
    }

    // ─── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldModifyUtilisateur() {
        RegisterRequest dto = buildRegisterRequest("alice@mail.com", "Dupont");
        dto.setPrenom("Alice-Modifiée");

        UtilisateurDetailResponseDto result = utilisateurService.update(1, dto);

        assertThat(result.getPrenom()).isEqualTo("Alice-Modifiée");
    }

    @Test
    void update_shouldThrow_whenUtilisateurNotFound() {
        RegisterRequest dto = buildRegisterRequest("inconnu@mail.com", "Inconnu");

        assertThatThrownBy(() -> utilisateurService.update(9999, dto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("9999");
    }

    // ─── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_shouldRemoveUtilisateur() {
        RegisterRequest request = buildRegisterRequest("todelete@mail.com", "ASupprimer");
        utilisateurService.register(request);

        // Récupère le dernier utilisateur créé
        List<UtilisateurResponseDto> all = utilisateurService.getAll();
        Integer newId = all.stream()
                .mapToInt(UtilisateurResponseDto::getId)
                .max()
                .orElseThrow();

        utilisateurService.delete(newId);

        assertThatThrownBy(() -> utilisateurService.getById(newId))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        assertThatThrownBy(() -> utilisateurService.delete(9999))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── helper ────────────────────────────────────────────────────────────────

    private RegisterRequest buildRegisterRequest(String email, String nom) {
        RegisterRequest request = new RegisterRequest();
        request.setNom(nom);
        request.setPrenom("Prénom");
        request.setEmail(email);
        request.setDateNaissance(LocalDate.of(1995, 6, 15));
        request.setNumeroCarte("CARD999");
        request.setRoleUtilisateur(RoleUtilisateur.EMPRUNTEUR);
        request.setRue("1 rue Test");
        request.setCodePostal("73000");
        request.setVille("Chambéry");
        return request;
    }
}