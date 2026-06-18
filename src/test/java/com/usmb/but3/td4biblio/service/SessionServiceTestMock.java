package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.repository.UserRepository;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private VaadinSession vaadinSession;
    @Mock private VaadinRequest vaadinRequest;
    @Mock private WrappedSession wrappedSession;

    private SessionService sessionService;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService(userRepository, passwordEncoder);

        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setEmail("alice@example.com");
        utilisateur.setHashMotDePasse("$2a$hashed");
        utilisateur.setRoleUtilisateur(RoleUtilisateur.BIBLIOTHECAIRE);

        SecurityContextHolder.clearContext();
    }

    // ─── Méthode utilitaire : capture le SecurityContext stocké en session ───

    /**
     * Après un appel à login(), le SecurityContext est stocké dans la WrappedSession
     * via setAttribute("SPRING_SECURITY_CONTEXT", context).
     * On le capture ici pour vérifier son contenu sans dépendre du SecurityContextHolder,
     * dont l'état interne peut diverger après clearContext().
     */
    private SecurityContext captureSecurityContext() {
        ArgumentCaptor<SecurityContext> captor = ArgumentCaptor.forClass(SecurityContext.class);
        verify(wrappedSession).setAttribute(eq("SPRING_SECURITY_CONTEXT"), captor.capture());
        return captor.getValue();
    }

    // ─── login ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login - enregistre l'utilisateur dans la session Vaadin")
    void login_enregistreUtilisateurEnSession() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class);
             MockedStatic<VaadinRequest> requestStatic = mockStatic(VaadinRequest.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            requestStatic.when(VaadinRequest::getCurrent).thenReturn(vaadinRequest);
            when(vaadinRequest.getWrappedSession()).thenReturn(wrappedSession);

            sessionService.login(utilisateur);

            verify(vaadinSession).setAttribute(Utilisateur.class, utilisateur);
        }
    }

    @Test
    @DisplayName("login - stocke une authentification avec le bon email dans le contexte Spring Security")
    void login_stockeAuthenticationAvecBonEmail() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class);
             MockedStatic<VaadinRequest> requestStatic = mockStatic(VaadinRequest.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            requestStatic.when(VaadinRequest::getCurrent).thenReturn(vaadinRequest);
            when(vaadinRequest.getWrappedSession()).thenReturn(wrappedSession);

            sessionService.login(utilisateur);

            // On lit le contexte capturé depuis la session, pas depuis SecurityContextHolder
            SecurityContext context = captureSecurityContext();
            Authentication auth = context.getAuthentication();

            assertThat(auth).isNotNull();
            assertThat(auth.getName()).isEqualTo("alice@example.com");
        }
    }

    @Test
    @DisplayName("login - attribue le rôle correct à l'authentification")
    void login_attribueRoleCorrect() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class);
             MockedStatic<VaadinRequest> requestStatic = mockStatic(VaadinRequest.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            requestStatic.when(VaadinRequest::getCurrent).thenReturn(vaadinRequest);
            when(vaadinRequest.getWrappedSession()).thenReturn(wrappedSession);

            sessionService.login(utilisateur);

            // Même approche : on inspecte le contexte stocké en session
            SecurityContext context = captureSecurityContext();
            boolean hasRole = context.getAuthentication().getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_BIBLIOTHECAIRE"));

            assertThat(hasRole).isTrue();
        }
    }

    // ─── logout ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("logout - efface l'utilisateur de la session Vaadin et invalide la session")
    void logout_effaceSessionEtInvalide() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getSession()).thenReturn(wrappedSession);

            sessionService.logout();

            verify(vaadinSession).setAttribute(Utilisateur.class, null);
            verify(wrappedSession).invalidate();
        }
    }

    @Test
    @DisplayName("logout - efface le contexte Spring Security")
    void logout_effaceContexteSecurite() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getSession()).thenReturn(wrappedSession);

            // On pre-charge un contexte pour vérifier qu'il est bien effacé
            SecurityContextHolder.getContext().setAuthentication(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            "alice@example.com", null
                    )
            );

            sessionService.logout();

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        }
    }

    // ─── getCurrentUser ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getCurrentUser - retourne l'utilisateur connecté s'il est présent en session")
    void getCurrentUser_retourneUtilisateurConnecte() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(utilisateur);

            Optional<Utilisateur> result = sessionService.getCurrentUser();

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
        }
    }

    @Test
    @DisplayName("getCurrentUser - retourne un Optional vide si aucun utilisateur en session")
    void getCurrentUser_retourneVideSiAucunUtilisateur() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(null);

            Optional<Utilisateur> result = sessionService.getCurrentUser();

            assertThat(result).isEmpty();
        }
    }

    // ─── isLoggedIn ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("isLoggedIn - retourne true si un utilisateur est en session")
    void isLoggedIn_retourneTrueSiConnecte() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(utilisateur);

            assertThat(sessionService.isLoggedIn()).isTrue();
        }
    }

    @Test
    @DisplayName("isLoggedIn - retourne false si aucun utilisateur n'est en session")
    void isLoggedIn_retourneFalseSiDeconnecte() {
        try (MockedStatic<VaadinSession> sessionStatic = mockStatic(VaadinSession.class)) {

            sessionStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
            when(vaadinSession.getAttribute(Utilisateur.class)).thenReturn(null);

            assertThat(sessionService.isLoggedIn()).isFalse();
        }
    }

    // ─── authenticate ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("authenticate - retourne l'utilisateur si email et mot de passe sont corrects")
    void authenticate_retourneUtilisateurSiCredentialsValides() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("motdepasse", "$2a$hashed")).thenReturn(true);

        Optional<Utilisateur> result = sessionService.authenticate("alice@example.com", "motdepasse");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("authenticate - retourne un Optional vide si le mot de passe est incorrect")
    void authenticate_retourneVideSiMotDePasseIncorrect() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("mauvais", "$2a$hashed")).thenReturn(false);

        Optional<Utilisateur> result = sessionService.authenticate("alice@example.com", "mauvais");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("authenticate - retourne un Optional vide si l'email est introuvable")
    void authenticate_retourneVideSiEmailInconnu() {
        when(userRepository.findByEmail("inconnu@example.com")).thenReturn(Optional.empty());

        Optional<Utilisateur> result = sessionService.authenticate("inconnu@example.com", "motdepasse");

        assertThat(result).isEmpty();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}