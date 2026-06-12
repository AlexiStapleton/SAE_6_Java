package com.usmb.but3.td4biblio.view.Users;

import com.usmb.but3.td4biblio.DTO.RegisterRequest;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.service.AuthService;
import com.usmb.but3.td4biblio.service.SessionService;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


@SpringComponent
@UIScope
public class RegisterEditor extends VerticalLayout implements KeyNotifier {

    private final AuthService authService;
    private final SessionService sessionService;

    private final TextField nom = new TextField("Nom");
    private final TextField prenom = new TextField("Prénom");
    private final EmailField email = new EmailField("Email");
    private final PasswordField password = new PasswordField("Mot de passe");
    private final DatePicker dateNaissance = new DatePicker("Date de naissance");
    private final TextField numeroCarte = new TextField("Numéro de carte");
    private final ComboBox<RoleUtilisateur> roleUtilisateur = new ComboBox<>("Rôle");

    private final Button saveBtn = new Button("Créer le compte");
    private final Button cancelBtn = new Button("Annuler");

    private final Binder<RegisterRequest> binder = new Binder<>(RegisterRequest.class);

    public RegisterEditor(AuthService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;

        roleUtilisateur.setItems(RoleUtilisateur.values());
        roleUtilisateur.setItemLabelGenerator(Enum::name);

        configureBinder();

        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveBtn.addClickListener(e -> save());
        cancelBtn.addClickListener(e -> clearForm());

        FormLayout form = new FormLayout(
                nom, prenom, email, password,
                dateNaissance,
                numeroCarte, roleUtilisateur
        );
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        setAlignItems(FlexComponent.Alignment.CENTER);
        setMaxWidth("700px");

        add(form, new HorizontalLayout(saveBtn, cancelBtn));
        clearForm();
    }

    private void configureBinder() {
        binder.forField(nom)
                .asRequired("Le nom est obligatoire")
                .bind(RegisterRequest::getNom, RegisterRequest::setNom);

        binder.forField(prenom)
                .asRequired("Le prénom est obligatoire")
                .bind(RegisterRequest::getPrenom, RegisterRequest::setPrenom);

        binder.forField(email)
                .asRequired("L'email est obligatoire")
                .withValidator(new EmailValidator("Email invalide"))
                .bind(RegisterRequest::getEmail, RegisterRequest::setEmail);

        binder.forField(password)
                .asRequired("Le mot de passe est obligatoire")
                .withValidator(p -> p.length() >= 8, "Minimum 8 caractères")
                .bind(RegisterRequest::getPassword, RegisterRequest::setPassword);

        binder.forField(dateNaissance)
                .asRequired("La date de naissance est obligatoire")
                .bind(RegisterRequest::getDateNaissance, RegisterRequest::setDateNaissance);

        binder.forField(numeroCarte)
                .bind(RegisterRequest::getNumeroCarte, RegisterRequest::setNumeroCarte);

        binder.forField(roleUtilisateur)
                .asRequired("Le rôle est obligatoire")
                .bind(RegisterRequest::getRoleUtilisateur, RegisterRequest::setRoleUtilisateur);
    }

    private void save() {
        try {
            RegisterRequest dto = new RegisterRequest();
            binder.writeBean(dto);

            Utilisateur created = authService.register(dto);

            sessionService.login(created);

            Notification.show("Compte créé avec succès !")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate("auteur");
        } catch (ValidationException e) {
            Notification.show("Veuillez corriger les erreurs du formulaire.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void clearForm() {
        binder.readBean(new RegisterRequest());
    }
}