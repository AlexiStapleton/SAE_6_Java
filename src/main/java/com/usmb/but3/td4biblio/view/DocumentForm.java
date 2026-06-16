package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.dto.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentFormDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.dto.DvdCreateDto;
import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.enumeration.TypeDocument;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.usmb.but3.td4biblio.service.CodeRaisonService;
import com.usmb.but3.td4biblio.service.DvdService;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.GenreDocumentService;
import com.usmb.but3.td4biblio.service.LivreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DocumentForm extends Dialog {

    private final LivreService livreService;
    private final DvdService dvdService;

    public DocumentForm(
        LivreService livreService,
        DvdService dvdService,
        AuteurService auteurService,
        EditeurService editeurService,
        BibliothequeService bibliothequeService,
        GenreDocumentService genreService,
        CodeRaisonService codeRaisonService
    ) {

        this.livreService = livreService;
        this.dvdService = dvdService;

        typeDocument.setItems(
                TypeDocument.values()
        );

        typeDocument.setItemLabelGenerator(
                TypeDocument::getLabel
        );

        auteur.setItems(
                auteurService.getAll()
        );

        auteur.setItemLabelGenerator(
                AuteurResponseDto::getDesc
        );

        editeur.setItems(
                editeurService.getAll()
        );

        editeur.setItemLabelGenerator(
                EditeurResponseDto::getNom
        );

        bibliotheque.setItems(
                bibliothequeService.getAll()
        );

        bibliotheque.setItemLabelGenerator(
                BibliothequeResponseDto::getNom
        );

        genre.setItems(
                genreService.getAll()
        );

        genre.setItemLabelGenerator(
                GenreDocumentResponseDto::getNom
        );

        codeRaison.setItems(
                codeRaisonService.getAll()
        );

        codeRaison.setItemLabelGenerator(
                CodeRaisonResponseDto::getNom
        );

        binder.bindInstanceFields(this);

        FormLayout layout =
                new FormLayout(
                        typeDocument,
                        auteur,
                        editeur,
                        bibliotheque,
                        genre,
                        codeRaison,
                        titre,
                        gif,
                        format,
                        description,
                        datePublication,
                        dateAcquisition,
                        codeEmplacement,
                        empruntable,
                        nbPages,
                        codeIsbn,
                        duree
                );

        add(layout);

        Button save =
                new Button("Enregistrer");

        Button cancel =
                new Button("Annuler");

        add(
                new HorizontalLayout(
                        save,
                        cancel
                )
        );

        save.addClickListener(
                e -> save()
        );

        cancel.addClickListener(
                e -> close()
        );

        typeDocument.addValueChangeListener(
                e -> updateFields()
        );
    }

    private Integer editingId;

    private final Binder<DocumentFormDto> binder =
        new Binder<>(DocumentFormDto.class);

    private DocumentFormDto dto;

    private final ComboBox<TypeDocument> typeDocument =
            new ComboBox<>("Type");

    private final TextField titre =
            new TextField("Titre");

    private final TextField gif =
        new TextField("GIF");

    private final TextField format =
            new TextField("Format");

    private final TextArea description =
            new TextArea("Description");

    private final DatePicker datePublication =
            new DatePicker("Publication");

    private final DatePicker dateAcquisition =
            new DatePicker("Acquisition");

    private final TextField codeEmplacement =
            new TextField("Emplacement");

    private final Checkbox empruntable =
            new Checkbox("Empruntable");

    private final IntegerField nbPages =
            new IntegerField("Nombre de pages");

    private final TextField codeIsbn =
            new TextField("ISBN");

    private final IntegerField duree =
            new IntegerField("Durée");

    private final ComboBox<AuteurResponseDto> auteur =
        new ComboBox<>("Auteur");

    private final ComboBox<EditeurResponseDto> editeur =
            new ComboBox<>("Éditeur");

    private final ComboBox<BibliothequeResponseDto> bibliotheque =
            new ComboBox<>("Bibliothèque");

    private final ComboBox<GenreDocumentResponseDto> genre =
            new ComboBox<>("Genre");

    private final ComboBox<CodeRaisonResponseDto> codeRaison =
            new ComboBox<>("Code raison");

    private void updateFields() {

        boolean livre =
                typeDocument.getValue()
                        == TypeDocument.LIVRE;

        boolean dvd =
                typeDocument.getValue()
                        == TypeDocument.DVD;

        nbPages.setVisible(livre);
        codeIsbn.setVisible(livre);

        duree.setVisible(dvd);
    }

    public void openCreateDialog() {

        editingId = null;

        dto = new DocumentFormDto();

        dto.setEmpruntable(true);

        binder.setBean(dto);

        open();
    }

    public void openEditDialog(
        DocumentResponseDto document
    ) {

        editingId = document.getId();

        dto = new DocumentFormDto();

        dto.setTitre(document.getTitre());
        dto.setGif(
                document.getGif()
        );
        dto.setFormat(document.getFormat());
        dto.setDescription(
                document.getDescription()
        );
        dto.setDatePublication(
                document.getDatePublication()
        );
        dto.setDateAcquisition(
                document.getDateAcquisition()
        );
        dto.setCodeEmplacement(
                document.getCodeEmplacement()
        );
        dto.setEmpruntable(
                document.getEmpruntable()
        );

        binder.setBean(dto);

        open();
    }

    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(
            ChangeHandler h
    ) {
        changeHandler = h;
    }

    private void save() {

        if (typeDocument.getValue()
                == TypeDocument.LIVRE) {

            LivreCreateDto dto =
                    new LivreCreateDto();

            fillCommon(dto);

            dto.setNbPages(
                    nbPages.getValue()
            );

            dto.setCodeIsbn(
                    codeIsbn.getValue()
            );

            if (editingId == null) {
                livreService.create(dto);
            }
            else {
                livreService.update(
                        editingId,
                        dto
                );
            }
        }
        else {

            DvdCreateDto dto =
                    new DvdCreateDto();

            fillCommon(dto);

            dto.setDuree(
                    duree.getValue()
            );

            if (editingId == null) {
                dvdService.create(dto);
            }
            else {
                dvdService.update(
                        editingId,
                        dto
                );
            }
        }

        close();

        if (changeHandler != null) {
            changeHandler.onChange();
        }
    }

    private void fillCommon(
            DocumentCreateDto dto
    ) {

        dto.setAuteurId(
                auteur.getValue() == null
                        ? null
                        : auteur.getValue().getId()
        );

        dto.setEditeurId(
                editeur.getValue() == null
                        ? null
                        : editeur.getValue().getId()
        );

        dto.setBibliothequeId(
                bibliotheque.getValue() == null
                        ? null
                        : bibliotheque.getValue().getId()
        );

        dto.setGenreId(
                genre.getValue() == null
                        ? null
                        : genre.getValue().getId()
        );

        dto.setCodeRaisonId(
                codeRaison.getValue() == null
                        ? null
                        : codeRaison.getValue().getId()
        );

        dto.setTitre(
                titre.getValue()
        );

        dto.setGif(
                gif.getValue()
        );

        dto.setFormat(
                format.getValue()
        );

        dto.setDescription(
                description.getValue()
        );

        dto.setDatePublication(
                datePublication.getValue()
        );

        dto.setDateAcquisition(
                dateAcquisition.getValue()
        );

        dto.setCodeEmplacement(
                codeEmplacement.getValue()
        );

        dto.setEmpruntable(
                empruntable.getValue()
        );
    }

}
