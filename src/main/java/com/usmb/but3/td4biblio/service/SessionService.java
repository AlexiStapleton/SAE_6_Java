package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SessionService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(Utilisateur user) {
        VaadinSession.getCurrent().setAttribute(Utilisateur.class, user);
    }

    public void logout() {
        VaadinSession.getCurrent().setAttribute(Utilisateur.class, null);
        VaadinSession.getCurrent().getSession().invalidate();
    }

    public Optional<Utilisateur> getCurrentUser() {
        return Optional.ofNullable(
                VaadinSession.getCurrent().getAttribute(Utilisateur.class)
        );
    }

    public boolean isLoggedIn() {
        return getCurrentUser().isPresent();
    }
    public Optional<Utilisateur> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getHashMotDePasse()));
    }
}