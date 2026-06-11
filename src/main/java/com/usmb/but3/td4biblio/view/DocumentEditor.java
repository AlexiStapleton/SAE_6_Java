package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.AuteurService;
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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DocumentEditor extends VerticalLayout implements KeyNotifier {

    private final DocumentService documentService;
    private final AuteurService auteurService;

    private Document document;

    // Champs Document
    TextField titre = new TextField("Titre");

    TextField format = new TextField("Format");

    TextField gif = new TextField("Gif");

    TextField codeEmplacement =
            new TextField("Code emplacement");

    TextArea description =
            new TextArea("Description");

    DatePicker datePublication =
            new DatePicker("Date publication");

    DatePicker dateAcquisition =
            new DatePicker("Date acquisition");

    Checkbox empruntable =
            new Checkbox("Empruntable");

    ComboBox<Auteur> auteurComboBox =
            new ComboBox<>("Auteur");

    // Boutons
    Button save =
            new Button("Sauvegarder",
                    VaadinIcon.CHECK.create());

    Button cancel =
            new Button("Annuler");

    Button delete =
            new Button("Supprimer",
                    VaadinIcon.TRASH.create());

    HorizontalLayout actions =
            new HorizontalLayout(save, cancel, delete);

    Binder<Document> binder =
            new Binder<>(Document.class);

    private ChangeHandler changeHandler;

    public DocumentEditor(
            AuteurService auteurService,
            DocumentService documentService) {

        this.auteurService = auteurService;
        this.documentService = documentService;

        auteurComboBox.setItems(
                auteurService.getAllAuteurs());

        auteurComboBox.setItemLabelGenerator(
                Auteur::getDesc);

        auteurComboBox.setPlaceholder(
                "Sélectionner un auteur");

        auteurComboBox.setClearButtonVisible(true);

        add(
                titre,
                auteurComboBox,
                format,
                gif,
                codeEmplacement,
                description,
                datePublication,
                dateAcquisition,
                empruntable,
                actions
        );

        binder.bindInstanceFields(this);

        binder.forField(auteurComboBox)
                .asRequired("Auteur obligatoire")
                .bind(
                        Document::getAuteur,
                        Document::setAuteur
                );

        save.addClickListener(e -> save());

        delete.addClickListener(e -> delete());

        cancel.addClickListener(
                e -> editDocument(document));

        addKeyPressListener(
                Key.ENTER,
                e -> save());

        setVisible(false);
    }

    void save() {

        if(document.getId() == null) {
            documentService.saveDocument(document);
        }
        else {
            documentService.updateDocument(document);
        }

        changeHandler.onChange();
    }

    void delete() {

        documentService.deleteDocument(
                document.getId());

        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editDocument(Document d) {

        if(d == null) {
            setVisible(false);
            return;
        }

        final boolean persisted =
                d.getId() != null;

        if(persisted) {
            document =
                    documentService.getDocumentById(
                            d.getId());
        }
        else {
            document = d;
        }

        cancel.setVisible(persisted);

        binder.setBean(document);

        setVisible(true);

        titre.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}