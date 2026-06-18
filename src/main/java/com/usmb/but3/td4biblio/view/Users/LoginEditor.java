package com.usmb.but3.td4biblio.view.Users;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.SessionService;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Optional;

@SpringComponent
@UIScope
public class LoginEditor extends VerticalLayout implements KeyNotifier {

    private final SessionService sessionService;

    private final EmailField email = new EmailField("Email");
    private final PasswordField password = new PasswordField("Mot de passe");
    private final Button loginBtn = new Button("Se connecter");

    public LoginEditor(SessionService sessionService) {
        this.sessionService = sessionService;

        loginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginBtn.addClickListener(e -> login());

        addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> login());

        setAlignItems(FlexComponent.Alignment.CENTER);
        setWidth("350px");
        add(email, password, loginBtn);
    }

    private void login() {
        Optional<Utilisateur> userOpt = sessionService.authenticate(email.getValue(), password.getValue());

        if (userOpt.isPresent()) {
            sessionService.login(userOpt.get());
            // Redirige vers la page d'accueil (MainView) après connexion.
            UI.getCurrent().navigate("");
        } else {
            Notification.show("Email ou mot de passe incorrect")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}