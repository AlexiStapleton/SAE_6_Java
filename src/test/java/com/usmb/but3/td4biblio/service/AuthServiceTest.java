package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder);

        utilisateur = new Utilisateur();
        utilisateur.setEmail("alice@mail.com");
        utilisateur.setHashMotDePasse("hashed_password");
    }

    // ─── authenticate ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("authenticate - retourne l'utilisateur si email et mot de passe corrects")
    void authenticate_retourneUtilisateur_siCredentielsValides() {
        when(userRepository.findByEmail("alice@mail.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);

        Optional<Utilisateur> result = authService.authenticate("alice@mail.com", "password123");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@mail.com");
    }

    @Test
    @DisplayName("authenticate - retourne vide si mot de passe incorrect")
    void authenticate_retourneVide_siMotDePasseIncorrect() {
        when(userRepository.findByEmail("alice@mail.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("mauvais_mdp", "hashed_password")).thenReturn(false);

        Optional<Utilisateur> result = authService.authenticate("alice@mail.com", "mauvais_mdp");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("authenticate - retourne vide si email inconnu")
    void authenticate_retourneVide_siEmailInconnu() {
        when(userRepository.findByEmail("inconnu@mail.com")).thenReturn(Optional.empty());

        Optional<Utilisateur> result = authService.authenticate("inconnu@mail.com", "password123");

        assertThat(result).isEmpty();
        // passwordEncoder ne doit jamais être appelé si l'utilisateur n'existe pas
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("authenticate - retourne vide si email et mot de passe tous les deux incorrects")
    void authenticate_retourneVide_siEmailEtMotDePasseIncorrects() {
        when(userRepository.findByEmail("inconnu@mail.com")).thenReturn(Optional.empty());

        Optional<Utilisateur> result = authService.authenticate("inconnu@mail.com", "mauvais_mdp");

        assertThat(result).isEmpty();
        verify(passwordEncoder, never()).matches(any(), any());
    }
}