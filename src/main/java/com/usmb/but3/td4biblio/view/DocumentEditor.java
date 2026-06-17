package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.component.html.Image;

/**
 * Éditeur de Document — même architecture qu'AuteurEditor.
 *
 * Travaille exclusivement avec DocumentDetailResponseDto pour l'affichage du détail
 * (récupéré via getById()) mais accepte un DocumentResponseDto en entrée (depuis la grille).
 *
 * Les fonctionnalités save/delete ne sont pas encore activées (hors périmètre sprint actuel).
 */
@SpringComponent
@UIScope
public class DocumentEditor extends VerticalLayout implements KeyNotifier {

    private final DocumentService documentService;

    /** Le DTO de détail actuellement affiché */
    private DocumentDetailResponseDto document;

    // ------------------------------------------------------------------
    // Champs du formulaire — nommés pour correspondre aux propriétés du DTO
    // ------------------------------------------------------------------
    TextField titre            = new TextField("Titre");
    TextField nomAuteur        = new TextField("Auteur");
    TextField nomEditeur       = new TextField("Éditeur");
    TextField nomBibliotheque  = new TextField("Bibliothèque");
    TextField nomGenreDocument = new TextField("Genre");
    TextField format           = new TextField("Format");
    Image gif = new Image();
    TextField codeEmplacement  = new TextField("Code emplacement");
    TextArea  description      = new TextArea("Description");
    DatePicker datePublication = new DatePicker("Date de publication");
    DatePicker dateAcquisition = new DatePicker("Date d'acquisition");
    Checkbox  empruntable      = new Checkbox("Empruntable");

    // ------------------------------------------------------------------
    // Boutons
    // ------------------------------------------------------------------
    Button cancel = new Button("Fermer", VaadinIcon.CLOSE.create());
    HorizontalLayout actions = new HorizontalLayout(cancel);

    Binder<DocumentDetailResponseDto> binder = new Binder<>(DocumentDetailResponseDto.class);

    private ChangeHandler changeHandler;

    public DocumentEditor(DocumentService documentService) {
        this.documentService = documentService;

        // Tous les champs en lecture seule (affichage uniquement pour ce sprint)
        titre.setReadOnly(true);
        nomAuteur.setReadOnly(true);
        nomEditeur.setReadOnly(true);
        nomBibliotheque.setReadOnly(true);
        nomGenreDocument.setReadOnly(true);
        format.setReadOnly(true);
        gif.setWidth("300px");
        gif.setHeight("300px");
        gif.getStyle()
            .set("object-fit", "contain");
        codeEmplacement.setReadOnly(true);
        description.setReadOnly(true);
        datePublication.setReadOnly(true);
        dateAcquisition.setReadOnly(true);
        empruntable.setReadOnly(true);

        add(
                titre,
                nomAuteur,
                nomEditeur,
                nomBibliotheque,
                nomGenreDocument,
                format,
                gif,
                codeEmplacement,
                description,
                datePublication,
                dateAcquisition,
                empruntable,
                actions
        );

        // Liaison automatique par convention de nommage
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> setVisible(false));
        addKeyPressListener(Key.ESCAPE, e -> setVisible(false));

        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    /**
     * Reçoit un DocumentResponseDto depuis la grille, charge le détail complet
     * via getById() et affiche le formulaire — exactement comme AuteurEditor.editAuteur().
     */
    public final void editDocument(DocumentResponseDto dto) {
    if (dto == null) {
        setVisible(false);
        return;
    }

        final boolean persisted = dto.getId() != null;

        if (persisted) {
            document = documentService.getById(dto.getId());
        } else {
            document = new DocumentDetailResponseDto();
        }

        cancel.setVisible(persisted);

        binder.setBean(document);

        String url = document.getGif();
        if (url != null && !url.isBlank()) {
            gif.setSrc(url);
            gif.setVisible(true);
        } else {
            gif.setVisible(false);
        }

        setVisible(true);
        titre.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}