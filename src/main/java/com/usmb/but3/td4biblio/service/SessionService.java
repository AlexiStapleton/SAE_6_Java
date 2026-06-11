package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

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
}