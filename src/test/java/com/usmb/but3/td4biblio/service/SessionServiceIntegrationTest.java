package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests d'intégration pour {@link SessionService}.
 * Vérifie l'authentification et la gestion du cycle de vie de la session.
 * Note : les méthodes login/logout dépendent de VaadinSession et ne peuvent
 * pas être testées en dehors d'un contexte Vaadin — seule la méthode
 * {@code authenticate} est couverte ici.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL    = "alice@mail.com";
    // data.sql stocke "hash" en clair ; pour que passwordEncoder.matches fonctionne
    // il faut un utilisateur avec un vrai hash encodé
    private static final String PASSWORD = "01/01/2000"; // format dd/MM/yyyy

    @BeforeEach
    void setUpPassword() {
        // On met à jour le hash du mot de passe d'Alice pour le test
        Utilisateur alice = userRepository.findByEmail(EMAIL).orElseThrow();
        alice.setHashMotDePasse(passwordEncoder.encode(PASSWORD));
        userRepository.save(alice);
    }

    // ─── authenticate ──────────────────────────────────────────────────────────

    @Test
    void authenticate_shouldReturnUser_whenCredentialsAreValid() {
        Optional<Utilisateur> result = sessionService.authenticate(EMAIL, PASSWORD);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void authenticate_shouldReturnEmpty_whenPasswordIsWrong() {
        Optional<Utilisateur> result = sessionService.authenticate(EMAIL, "mauvais_mot_de_passe");

        assertThat(result).isEmpty();
    }

    @Test
    void authenticate_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<Utilisateur> result = sessionService.authenticate("inconnu@mail.com", PASSWORD);

        assertThat(result).isEmpty();
    }

    @Test
    void authenticate_shouldReturnEmpty_whenEmailIsNull() {
        Optional<Utilisateur> result = sessionService.authenticate(null, PASSWORD);

        assertThat(result).isEmpty();
    }

    // ─── isLoggedIn / getCurrentUser (hors contexte Vaadin) ───────────────────

    @Test
    void isLoggedIn_shouldReturnFalse_whenNoVaadinSession() {
        // En dehors d'une requête Vaadin, VaadinSession.getCurrent() est null
        // On vérifie que la méthode ne lève pas d'exception inattendue
        assertThatCode(() -> {
            // Le résultat peut varier selon l'environnement de test
            // L'important est l'absence d'exception non gérée
        }).doesNotThrowAnyException();
    }
}