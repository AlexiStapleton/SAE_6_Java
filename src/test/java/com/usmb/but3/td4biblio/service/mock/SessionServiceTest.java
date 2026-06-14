package com.usmb.but3.td4biblio.service.mock;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UserRepository;
import com.usmb.but3.td4biblio.service.SessionService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private VaadinSession vaadinSession;
    @Mock private WrappedSession wrappedSession;

    private SessionService sessionService;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService(userRepository, passwordEncoder);

        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setEmail("jean.dupont@email.com");
        utilisateur.setHashMotDePasse("hashed_password");
    }

    // ─── authenticate ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("authenticate - retourne l'utilisateur si email et mot de passe corrects")
    void authenticate_retourneUtilisateur() {
        when(userRepository.findByEmail("jean.dupont@email.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("01/01/1990", "hashed_password")).thenReturn(true);

        Optional<Utilisateur> result = sessionService.authenticate("jean.dupont@email.com", "01/01/1990");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("jean.dupont@email.com");
    }

    @Test
    @DisplayName("authenticate - retourne vide si l'email est inconnu")
    void authenticate_retourneVideSiEmailInconnu() {
        when(userRepository.findByEmail("inconnu@email.com")).thenReturn(Optional.empty());

        Optional<Utilisateur> result = sessionService.authenticate("inconnu@email.com", "password");

        assertThat(result).isEmpty();
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("authenticate - retourne vide si le mot de passe est incorrect")
    void authenticate_retourneVideSiMotDePasseIncorrect() {
        when(userRepository.findByEmail("jean.dupont@email.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("mauvais", "hashed_password")).thenReturn(false);

        Optional<Utilisateur> result = sessionService.authenticate("jean.dupont@email.com", "mauvais");

        assertThat(result).isEmpty();
    }

    // ─── login / logout / getCurrentUser / isLoggedIn ─────────────────────────

    @Test
    @DisplayName("login - stocke l'utilisateur dans la session Vaadin")
    void login_stockeUtilisateur() {
        try (MockedStatic<VaadinSession> mockedStatic = mockStatic(VaadinSession.class)) {
            mockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);

            sessionService.login(utilisateur);

            verify(vaadinSession).setAttribute(Utilisateur.class, utilisateur);
        }
    }

    @Test
    @DisplayName("logout - efface l'utilisateur et invalide la session")
    void logout_effaceSessionEtInvalidate() {
        try (MockedStatic<VaadinSession> mockedStatic = mockStatic(VaadinSession.class)) {
            mockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getSession()).thenReturn(wrappedSession);

            sessionService.logout();

            verify(vaadinSession).setAttribute(Utilisateur.class, null);
            verify(wrappedSession).invalidate();
        }
    }

    @Test
    @DisplayName("getCurrentUser - retourne l'utilisateur connecté")
    void getCurrentUser_retourneUtilisateur() {
        try (MockedStatic<VaadinSession> mockedStatic = mockStatic(VaadinSession.class)) {
            mockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(utilisateur);

            Optional<Utilisateur> result = sessionService.getCurrentUser();

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("jean.dupont@email.com");
        }
    }

    @Test
    @DisplayName("getCurrentUser - retourne vide si aucun utilisateur connecté")
    void getCurrentUser_retourneVideSiNonConnecte() {
        try (MockedStatic<VaadinSession> mockedStatic = mockStatic(VaadinSession.class)) {
            mockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(null);

            Optional<Utilisateur> result = sessionService.getCurrentUser();

            assertThat(result).isEmpty();
        }
    }

    @Test
    @DisplayName("isLoggedIn - retourne true si un utilisateur est connecté")
    void isLoggedIn_retourneTrueSiConnecte() {
        try (MockedStatic<VaadinSession> mockedStatic = mockStatic(VaadinSession.class)) {
            mockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(utilisateur);

            assertThat(sessionService.isLoggedIn()).isTrue();
        }
    }

    @Test
    @DisplayName("isLoggedIn - retourne false si aucun utilisateur connecté")
    void isLoggedIn_retourneFalseSiNonConnecte() {
        try (MockedStatic<VaadinSession> mockedStatic = mockStatic(VaadinSession.class)) {
            mockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(null);

            assertThat(sessionService.isLoggedIn()).isFalse();
        }
    }
}
