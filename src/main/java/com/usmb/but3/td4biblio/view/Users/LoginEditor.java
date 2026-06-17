package com.usmb.but3.td4biblio.view.Users;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.SessionService;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
            UI.getCurrent().navigate("auteur");
            checkAbonnementExpiration(userOpt.get());
        } else {
            Notification.show("Email ou mot de passe incorrect")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void checkAbonnementExpiration(Utilisateur user) {
        if (user.getDateFinAbonnement() == null) return;

        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusWeeks(2);

        if (!user.getDateFinAbonnement().isAfter(limit)) {
            Dialog dialog = new Dialog();
            dialog.add(new H2("⚠️ Attention"));
            dialog.add(new Paragraph("Votre abonnement est sur le point d'expirer."));

            Button closeBtn = new Button("OK", e -> dialog.close());
            closeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            HorizontalLayout btnLayout = new HorizontalLayout(closeBtn);
            btnLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            btnLayout.setWidthFull();
            dialog.add(btnLayout);

            dialog.setCloseOnOutsideClick(false);
            dialog.open();
        }
    }
}