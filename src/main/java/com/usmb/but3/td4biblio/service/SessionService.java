package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UserRepository;
import com.vaadin.flow.server.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des sessions utilisateur.
 * Gère l'authentification, la connexion et la déconnexion des utilisateurs.
 * Interagit avec Spring Security et Vaadin pour maintenir les contextes de sécurité et de session.
 */
@Service
public class SessionService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur du service de session.
     * Injecte les dépendances nécessaires pour la gestion des utilisateurs et du chiffrage des mots de passe.
     * @param userRepository - repository des utilisateurs
     * @param passwordEncoder - encodeur de mots de passe pour la sécurité
     */
    public SessionService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Établit une session de connexion pour un utilisateur.
     * Crée et enregistre l'authentification dans le contexte de sécurité Spring.
     * @param user - l'utilisateur à connecter
     */
    public void login(Utilisateur user) {
        VaadinSession.getCurrent().setAttribute(Utilisateur.class, user);
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRoleUtilisateur().name())
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, authorities
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        VaadinRequest.getCurrent().getWrappedSession()
                .setAttribute("SPRING_SECURITY_CONTEXT", context);
    }

    /**
     * Termine la session de l'utilisateur actuellement connecté.
     * Invalide la session Vaadin et efface le contexte de sécurité Spring.
     */
    public void logout() {
        VaadinSession.getCurrent().setAttribute(Utilisateur.class, null);
        VaadinSession.getCurrent().getSession().invalidate();
        SecurityContextHolder.clearContext();
    }

    /**
     * Récupère l'utilisateur actuellement connecté.
     * @return un Optional contenant l'utilisateur actuel ou vide s'il n'existe pas
     */
    public Optional<Utilisateur> getCurrentUser() {
        return Optional.ofNullable(
                VaadinSession.getCurrent().getAttribute(Utilisateur.class)
        );
    }

    /**
     * Vérifie si un utilisateur est actuellement connecté.
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean isLoggedIn() {
        return getCurrentUser().isPresent();
    }

    /**
     * Authentifie un utilisateur à partir de ses identifiants.
     * Vérifie que le mot de passe correspond à celui stocké dans la base de données.
     * @param email - adresse email de l'utilisateur
     * @param password - mot de passe en clair à vérifier
     * @return un Optional contenant l'utilisateur si l'authentification réussit, vide sinon
     */
    public Optional<Utilisateur> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getHashMotDePasse()));
    }
}