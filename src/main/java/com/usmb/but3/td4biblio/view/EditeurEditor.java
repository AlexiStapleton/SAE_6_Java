package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.DTO.AdresseResponseDto;
import com.usmb.but3.td4biblio.DTO.EditeurCreateDto;
import com.usmb.but3.td4biblio.DTO.EditeurResponseDto;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;

@SpringComponent
@UIScope
public class EditeurEditor extends VerticalLayout implements KeyNotifier {

    private final EditeurService editeurService;

    private EditeurResponseDto editeur;

    private final H3 formTitle = new H3("Fiche éditeur");

    TextField nom = new TextField("Nom de la société");
    TextField lienSiteWeb = new TextField("Site web");
    TextField lienWikipedia = new TextField("Page Wikipédia");
    TextField rue = new TextField("Rue");
    TextField codePostal = new TextField("Code postal");
    TextField ville = new TextField("Ville");

    FormLayout liensFields = new FormLayout(lienSiteWeb, lienWikipedia);
    FormLayout adresseFields = new FormLayout(rue, codePostal, ville);

    Button save = new Button("Enregistrer", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<EditeurResponseDto> binder = new Binder<>(EditeurResponseDto.class);
    private ChangeHandler changeHandler;

    public EditeurEditor(EditeurService editeurService) {
        this.editeurService = editeurService;

        // --- Style "carte moderne" ---
        addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Padding.LARGE,
                LumoUtility.Margin.Top.MEDIUM
        );
        formTitle.addClassNames(LumoUtility.Margin.NONE, LumoUtility.TextColor.PRIMARY);

        nom.setWidthFull();
        nom.setPrefixComponent(VaadinIcon.BUILDING.create());
        lienSiteWeb.setPrefixComponent(VaadinIcon.GLOBE.create());
        lienWikipedia.setPrefixComponent(VaadinIcon.INFO_CIRCLE.create());
        rue.setPrefixComponent(VaadinIcon.HOME.create());
        codePostal.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        ville.setPrefixComponent(VaadinIcon.MAP_MARKER.create());

        liensFields.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        adresseFields.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3)
        );

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(formTitle, nom, liensFields, adresseFields, actions);

        // --- Binding des champs simples ---
        binder.forField(nom)
                .asRequired("Le nom est obligatoire")
                .bind(EditeurResponseDto::getNom, EditeurResponseDto::setNom);

        binder.forField(lienSiteWeb)
                .bind(EditeurResponseDto::getLienSiteWeb, EditeurResponseDto::setLienSiteWeb);

        binder.forField(lienWikipedia)
                .bind(EditeurResponseDto::getLienWikipedia, EditeurResponseDto::setLienWikipedia);

        // --- Binding "manuel" vers l'adresse imbriquée ---
        binder.forField(rue)
                .bind(e -> e.getAdresse() != null ? e.getAdresse().getRue() : "",
                        (e, v) -> ensureAdresse(e).setRue(v));

        binder.forField(codePostal)
                .bind(e -> e.getAdresse() != null ? e.getAdresse().getCodePostal() : "",
                        (e, v) -> ensureAdresse(e).setCodePostal(v));

        binder.forField(ville)
                .bind(e -> e.getAdresse() != null ? e.getAdresse().getVille() : "",
                        (e, v) -> ensureAdresse(e).setVille(v));

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEditeur(editeur));

        setVisible(false);
    }

    private AdresseResponseDto ensureAdresse(EditeurResponseDto e) {
        if (e.getAdresse() == null) {
            e.setAdresse(new AdresseResponseDto());
        }
        return e.getAdresse();
    }

    void delete() {
        editeurService.delete(editeur.getId());
        changeHandler.onChange();
    }

    void save() {
        EditeurCreateDto dto = new EditeurCreateDto(
                editeur.getNom(),
                editeur.getLienSiteWeb(),
                editeur.getLienWikipedia(),
                editeur.getAdresse().getId()
        );

        if (editeur.getId() == null) {
            editeurService.create(dto);
        } else {
            editeurService.update(editeur.getId(), dto);
        }
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEditeur(EditeurResponseDto e) {
        if (e == null) {
            setVisible(false);
            return;
        }

        final boolean persisted = e.getId() != null;
        if (persisted) {
            editeur = editeurService.getById(e.getId());
        } else {
            editeur = e;
            if (editeur.getAdresse() == null) {
                editeur.setAdresse(new AdresseResponseDto());
            }
        }

        cancel.setVisible(persisted);
        delete.setVisible(persisted);

        binder.setBean(editeur);
        setVisible(true);
        nom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}