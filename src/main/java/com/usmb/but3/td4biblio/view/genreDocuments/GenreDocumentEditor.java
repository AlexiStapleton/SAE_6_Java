package com.usmb.but3.td4biblio.view.genreDocuments;

import com.usmb.but3.td4biblio.dto.GenreDocumentCreateDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.service.GenreDocumentService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Éditeur inline de GenreDocument — affiché sous la grille.
 * Permet la création et la modification d'un genre de document.
 * Architecture calquée sur DocumentEditor.
 */
@SpringComponent
@UIScope
public class GenreDocumentEditor extends VerticalLayout implements KeyNotifier {

    private final GenreDocumentService genreDocumentService;

    /** Genre en cours d'édition (null si création) */
    private GenreDocumentResponseDto genre;

    // ------------------------------------------------------------------
    // Champs du formulaire
    // ------------------------------------------------------------------
    TextField nom = new TextField("Nom du genre");

    // ------------------------------------------------------------------
    // Boutons
    // ------------------------------------------------------------------
    Button save   = new Button("Enregistrer", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler",      VaadinIcon.CLOSE.create());
    Button delete = new Button("Supprimer",    VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    // ------------------------------------------------------------------
    // Binder
    // ------------------------------------------------------------------
    private final Binder<GenreDocumentCreateDto> binder = new Binder<>(GenreDocumentCreateDto.class);

    private ChangeHandler changeHandler;

    public GenreDocumentEditor(GenreDocumentService genreDocumentService) {
        this.genreDocumentService = genreDocumentService;

        add(nom, actions);

        // Binding automatique par nom de propriété
        binder.bindInstanceFields(this);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Raccourcis clavier
        addKeyPressListener(Key.ENTER,  e -> save());
        addKeyPressListener(Key.ESCAPE, e -> setVisible(false));

        save.addClickListener(  e -> save());
        cancel.addClickListener(e -> setVisible(false));
        delete.addClickListener(e -> delete());

        setVisible(false);
    }

    // ------------------------------------------------------------------
    // Actions
    // ------------------------------------------------------------------

    private void save() {
        if (!binder.validate().isOk()) {
            return;
        }

        GenreDocumentCreateDto dto = binder.getBean();

        if (genre.getId() == null) {
            // Création
            genreDocumentService.create(dto);
        } else {
            // Modification
            genreDocumentService.update(genre.getId(), dto);
        }

        if (changeHandler != null) {
            changeHandler.onChange();
        }
    }

    private void delete() {
        genreDocumentService.delete(genre.getId());
        if (changeHandler != null) {
            changeHandler.onChange();
        }
    }

    // ------------------------------------------------------------------
    // Ouverture de l'éditeur
    // ------------------------------------------------------------------

    /**
     * Ouvre l'éditeur pour créer un nouveau genre (DTO vide).
     */
    public void editGenre(GenreDocumentResponseDto g) {
        if (g == null) {
            setVisible(false);
            return;
        }

        genre = g;

        GenreDocumentCreateDto dto = new GenreDocumentCreateDto();
        dto.setNom(g.getNom() != null ? g.getNom() : "");

        binder.setBean(dto);

        boolean isNew = (g.getId() == null);
        delete.setVisible(!isNew);

        setVisible(true);
        nom.focus();
    }

    // ------------------------------------------------------------------
    // ChangeHandler
    // ------------------------------------------------------------------

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}