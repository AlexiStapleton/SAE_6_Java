package com.usmb.but3.td4biblio.Configs;

import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthenticationInitializer implements VaadinServiceInitListener {

    private static final Set<String> PUBLIC_ROUTES = Set.of("login", "register");

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().addBeforeEnterListener(this::beforeEnter)
        );
    }

    private void beforeEnter(BeforeEnterEvent event) {
        String path = event.getLocation().getFirstSegment();

        Optional<Utilisateur> user = Optional.ofNullable(
                VaadinSession.getCurrent().getAttribute(Utilisateur.class)
        );

        boolean isLoggedIn = user.isPresent();

        if (!isLoggedIn && !PUBLIC_ROUTES.contains(path)) {
            event.forwardTo("login");
            return;
        }

        if (path.equals("register")) {
            boolean isBibliothecaire = user
                    .map(u -> u.getRoleUtilisateur() == RoleUtilisateur.BIBLIOTHECAIRE)
                    .orElse(false);

            if (!isBibliothecaire) {
                event.forwardTo("auteur");
            }
        }
    }
}