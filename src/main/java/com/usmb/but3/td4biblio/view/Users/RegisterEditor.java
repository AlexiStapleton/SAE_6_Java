package com.usmb.but3.td4biblio.view.Users;

import com.usmb.but3.td4biblio.DTO.RegisterRequest;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.service.SessionService;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.KeyNotifier;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


@SpringComponent
@UIScope
public class RegisterEditor extends VerticalLayout implements KeyNotifier {

    private final UtilisateurService utilisateurService;
    private final SessionService sessionService;

    private final TextField nom = new TextField("Nom");
    private final TextField prenom = new TextField("Prénom");
    private final EmailField email = new EmailField("Email");
    private final DatePicker dateNaissance = new DatePicker("Date de naissance");
    private final TextField numeroCarte = new TextField("Numéro de carte");
    private final ComboBox<RoleUtilisateur> roleUtilisateur = new ComboBox<>("Rôle");

    // Champs adresse
    private final TextField rue = new TextField("Rue");
    private final TextField codePostal = new TextField("Code postal");
    private final TextField ville = new TextField("Ville");

    private final Button saveBtn = new Button("Créer le compte");
    private final Button cancelBtn = new Button("Annuler");

    private final Binder<RegisterRequest> binder = new Binder<>(RegisterRequest.class);

    public RegisterEditor(UtilisateurService utilisateurService, SessionService sessionService) {
        this.utilisateurService = utilisateurService;
        this.sessionService = sessionService;

        roleUtilisateur.setItems(RoleUtilisateur.values());
        roleUtilisateur.setItemLabelGenerator(Enum::name);

        configureBinder();

        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveBtn.addClickListener(e -> save());
        cancelBtn.addClickListener(e -> clearForm());

        FormLayout form = new FormLayout(
                nom, prenom, email,
                dateNaissance,
                numeroCarte, roleUtilisateur,
                rue, codePostal, ville
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

        binder.forField(dateNaissance)
                .asRequired("La date de naissance est obligatoire")
                .bind(RegisterRequest::getDateNaissance, RegisterRequest::setDateNaissance);

        binder.forField(numeroCarte)
                .bind(RegisterRequest::getNumeroCarte, RegisterRequest::setNumeroCarte);

        binder.forField(roleUtilisateur)
                .asRequired("Le rôle est obligatoire")
                .bind(RegisterRequest::getRoleUtilisateur, RegisterRequest::setRoleUtilisateur);

        binder.forField(rue)
                .asRequired("La rue est obligatoire")
                .bind(RegisterRequest::getRue, RegisterRequest::setRue);

        binder.forField(codePostal)
                .asRequired("Le code postal est obligatoire")
                .bind(RegisterRequest::getCodePostal, RegisterRequest::setCodePostal);

        binder.forField(ville)
                .asRequired("La ville est obligatoire")
                .bind(RegisterRequest::getVille, RegisterRequest::setVille);
    }

    private void save() {
        try {
            RegisterRequest dto = new RegisterRequest();
            binder.writeBean(dto);

            utilisateurService.register(dto);
            Notification.show("Compte créé avec succès !")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            clearForm();
        } catch (ValidationException e) {
            Notification.show("Veuillez corriger les erreurs du formulaire.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void clearForm() {
        binder.readBean(new RegisterRequest());
    }
}