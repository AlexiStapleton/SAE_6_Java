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

    public void logout() {
        VaadinSession.getCurrent().setAttribute(Utilisateur.class, null);
        VaadinSession.getCurrent().getSession().invalidate();
        SecurityContextHolder.clearContext();
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