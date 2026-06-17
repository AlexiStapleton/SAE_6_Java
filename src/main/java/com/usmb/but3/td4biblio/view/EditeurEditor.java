package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.DTO.AdresseCreateDto;
import com.usmb.but3.td4biblio.DTO.AdresseResponseDto;
import com.usmb.but3.td4biblio.DTO.EditeurCreateDto;
import com.usmb.but3.td4biblio.DTO.EditeurResponseDto;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.SessionService;
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
import lombok.Setter;

@SpringComponent
@UIScope
public class EditeurEditor extends VerticalLayout implements KeyNotifier {

    private final EditeurService editeurService;
    private final SessionService sessionService;

    private EditeurResponseDto editeur;

    // Snapshot des valeurs avant édition, pour détecter les changements
    private String snapshotNom;
    private String snapshotLienSiteWeb;
    private String snapshotLienWikipedia;
    private String snapshotRue;
    private String snapshotCodePostal;
    private String snapshotVille;

    TextField nom          = new TextField("Nom de la société");
    TextField lienSiteWeb  = new TextField("Site web");
    TextField lienWikipedia = new TextField("Page Wikipédia");
    TextField rue          = new TextField("Rue");
    TextField codePostal   = new TextField("Code postal");
    TextField ville        = new TextField("Ville");

    FormLayout liensFields   = new FormLayout(lienSiteWeb, lienWikipedia);
    FormLayout adresseFields = new FormLayout(rue, codePostal, ville);

    // Boutons mode lecture
    Button edit   = new Button("Modifier", VaadinIcon.EDIT.create());

    // Boutons mode édition
    Button save   = new Button("Enregistrer", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout readActions  = new HorizontalLayout(edit);
    HorizontalLayout editActions  = new HorizontalLayout(save, cancel, delete);

    Binder<EditeurResponseDto> binder = new Binder<>(EditeurResponseDto.class);
    @Setter
    private ChangeHandler changeHandler;

    public EditeurEditor(EditeurService editeurService, SessionService sessionService) {
        this.editeurService = editeurService;
        this.sessionService = sessionService;

        // --- Style "carte moderne" ---
        addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Padding.LARGE,
                LumoUtility.Margin.Top.MEDIUM
        );
        H3 formTitle = new H3("Fiche éditeur");
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

        // Styles boutons
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(formTitle, nom, liensFields, adresseFields, readActions, editActions);

        // --- Bindings ---
        binder.forField(nom)
                .bind(EditeurResponseDto::getNom, EditeurResponseDto::setNom);

        binder.forField(lienSiteWeb)
                .bind(EditeurResponseDto::getLienSiteWeb, EditeurResponseDto::setLienSiteWeb);

        binder.forField(lienWikipedia)
                .bind(EditeurResponseDto::getLienWikipedia, EditeurResponseDto::setLienWikipedia);

        binder.forField(rue)
                .bind(e -> e.getAdresse() != null ? e.getAdresse().getRue() : "",
                        (e, v) -> ensureAdresse(e).setRue(v));

        binder.forField(codePostal)
                .bind(e -> e.getAdresse() != null ? e.getAdresse().getCodePostal() : "",
                        (e, v) -> ensureAdresse(e).setCodePostal(v));

        binder.forField(ville)
                .bind(e -> e.getAdresse() != null ? e.getAdresse().getVille() : "",
                        (e, v) -> ensureAdresse(e).setVille(v));

        // --- Listeners ---
        addKeyPressListener(Key.ENTER, e -> { if (nom.isEnabled()) save(); });

        edit.addClickListener(e -> enterEditMode());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancelEdit());

        setVisible(false);
    }

    // ------------------------------------------------------------------ //
    //  Modes                                                               //
    // ------------------------------------------------------------------ //

    /** Bascule en lecture seule (après sélection d'un éditeur existant). */
    private void enterReadMode() {
        setFieldsEnabled(false);
        readActions.setVisible(isBibliothecaire());
        editActions.setVisible(false);
    }

    /** Bascule en mode édition. */
    private void enterEditMode() {
        takeSnapshot();
        setFieldsEnabled(true);
        readActions.setVisible(false);
        editActions.setVisible(true);
        // Le bouton Supprimer n'est visible que pour un éditeur déjà persisté
        delete.setVisible(editeur.getId() != null);
        nom.focus();
    }

    private void setFieldsEnabled(boolean enabled) {
        nom.setEnabled(enabled);
        lienSiteWeb.setEnabled(enabled);
        lienWikipedia.setEnabled(enabled);
        rue.setEnabled(enabled);
        codePostal.setEnabled(enabled);
        ville.setEnabled(enabled);
    }

    // ------------------------------------------------------------------ //
    //  Snapshot (détection de changement)                                  //
    // ------------------------------------------------------------------ //

    /**
     * Snapshot pris sur les valeurs des champs UI au moment où on entre en mode édition.
     * On ne snapshote PAS le bean car le binder live-binding l'écrase dès la première frappe.
     */
    private void takeSnapshot() {
        snapshotNom           = nom.getValue();
        snapshotLienSiteWeb   = lienSiteWeb.getValue();
        snapshotLienWikipedia = lienWikipedia.getValue();
        snapshotRue           = rue.getValue();
        snapshotCodePostal    = codePostal.getValue();
        snapshotVille         = ville.getValue();
    }

    private boolean hasChanged() {
        return eq(snapshotNom, nom.getValue())
            || eq(snapshotLienSiteWeb, lienSiteWeb.getValue())
            || eq(snapshotLienWikipedia, lienWikipedia.getValue())
            || eq(snapshotRue, rue.getValue())
            || eq(snapshotCodePostal, codePostal.getValue())
            || eq(snapshotVille, ville.getValue());
    }

    private static boolean eq(String a, String b) {
        // Traite null et "" comme équivalents pour éviter les faux positifs
        String sa = a == null ? "" : a;
        String sb = b == null ? "" : b;
        return !sa.equals(sb);
    }

    // ------------------------------------------------------------------ //
    //  Actions                                                             //
    // ------------------------------------------------------------------ //

    void delete() {
        editeurService.delete(editeur.getId());
        changeHandler.onChange();
    }

    void save() {
        // Validation manuelle : le binder ne peut pas valider des champs disabled
        if (nom.getValue() == null || nom.getValue().isBlank()) {
            nom.setInvalid(true);
            nom.setErrorMessage("Le nom est obligatoire");
            return;
        }
        nom.setInvalid(false);

        boolean isNew = editeur.getId() == null;

        if (!isNew && !hasChanged()) {
            // Aucune modification : retour en lecture sans appel API
            enterReadMode();
            return;
        }

        AdresseCreateDto adresseDto = new AdresseCreateDto(
                rue.getValue(),
                codePostal.getValue(),
                ville.getValue()
        );

        EditeurCreateDto dto = new EditeurCreateDto(
                nom.getValue(),
                lienSiteWeb.getValue(),
                lienWikipedia.getValue(),
                adresseDto
        );

        if (isNew) {
            editeurService.create(dto);
        } else {
            editeurService.update(editeur.getId(), dto);
        }
        changeHandler.onChange();
    }

    /** Annuler : on recharge les données d'origine et on repasse en lecture. */
    private void cancelEdit() {
        if (editeur.getId() != null) {
            editeur = editeurService.getById(editeur.getId());
            binder.setBean(editeur);
        }
        enterReadMode();
    }

    // ------------------------------------------------------------------ //
    //  Point d'entrée public                                               //
    // ------------------------------------------------------------------ //

    public interface ChangeHandler {
        void onChange();
    }

    /**
     * Ouvre la fiche pour un éditeur donné.
     * - Si l'éditeur est null → on cache le panneau.
     * - Si l'éditeur est nouveau (id == null) → mode édition directement.
     * - Si l'éditeur est existant → mode lecture seule.
     */
    public final void editEditeur(EditeurResponseDto e) {
        if (e == null) {
            setVisible(false);
            return;
        }

        final boolean isNew = e.getId() == null;
        if (isNew) {
            editeur = e;
            if (editeur.getAdresse() == null) {
                editeur.setAdresse(new AdresseResponseDto());
            }
        } else {
            editeur = editeurService.getById(e.getId());
        }

        binder.setBean(editeur);
        setVisible(true);

        if (isNew) {
            enterEditMode();
        } else {
            enterReadMode();
        }
    }

    // ------------------------------------------------------------------ //
    //  Utilitaires                                                         //
    // ------------------------------------------------------------------ //

    private AdresseResponseDto ensureAdresse(EditeurResponseDto e) {
        if (e.getAdresse() == null) {
            e.setAdresse(new AdresseResponseDto());
        }
        return e.getAdresse();
    }

    private boolean isBibliothecaire() {
        return sessionService.getCurrentUser()
                .map(u -> u.getRoleUtilisateur() == Utilisateur.RoleUtilisateur.BIBLIOTHECAIRE)
                .orElse(false);
    }
}