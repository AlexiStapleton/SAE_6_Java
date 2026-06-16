package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.usmb.but3.td4biblio.enumeration.ChampRechercheDocument;
import com.usmb.but3.td4biblio.enumeration.TypeRecherche;
import com.vaadin.flow.component.combobox.ComboBox;

/**
 * Vue principale Documents 
 */
@Component
@Scope("prototype")
@Route(value = "documents")
@PageTitle("Documents")
@Menu(title = "Documents", order = 1, icon = "vaadin:file-text")
public class DocumentView extends VerticalLayout {

    private final DocumentService documentService;
    final DocumentEditor editor;

    final Grid<DocumentResponseDto> grid;
    final TextField filter;
    final ComboBox<TypeRecherche> typeRecherche;
    final ComboBox<ChampRechercheDocument> champRecherche;
    private final Button addNewBtn;

    public DocumentView(DocumentService documentService, DocumentEditor editor) {
        this.documentService = documentService;
        this.editor = editor;

        this.grid      = new Grid<>(DocumentResponseDto.class);
        this.filter    = new TextField();
        this.typeRecherche = new ComboBox<>("Type de recherche");
        this.champRecherche = new ComboBox<>("Champ");
        this.addNewBtn = new Button("Nouveau document", VaadinIcon.PLUS.create());

        // ------------------------------------------------------------------
        // Layout
        // ------------------------------------------------------------------
        HorizontalLayout actions =
        new HorizontalLayout(
                champRecherche,
                filter,
                typeRecherche,
                addNewBtn
        );
        add(actions, grid, editor);

        // ------------------------------------------------------------------
        // Grille
        // ------------------------------------------------------------------
        grid.setHeight("400px");
        grid.setColumns("id", "titre", "nomAuteur", "nomEditeur", "datePublication", "empruntable");
        grid.getColumnByKey("id").setWidth("80px").setFlexGrow(0);

        // Libellés des colonnes en français
        grid.getColumnByKey("nomAuteur").setHeader("Auteur");
        grid.getColumnByKey("nomEditeur").setHeader("Éditeur");
        grid.getColumnByKey("datePublication").setHeader("Publication");
        grid.getColumnByKey("empruntable").setHeader("Empruntable");

        // ------------------------------------------------------------------
        // Filtre
        // ------------------------------------------------------------------
        champRecherche.setItems(
                ChampRechercheDocument.values()
        );

        champRecherche.setItemLabelGenerator(
                ChampRechercheDocument::getLabel
        );

        champRecherche.setValue(
                ChampRechercheDocument.TITRE
        );

        typeRecherche.setItems(TypeRecherche.values());
        typeRecherche.setItemLabelGenerator(TypeRecherche::getLabel);
        typeRecherche.setValue(TypeRecherche.CONTIENT);
        
        filter.setPlaceholder("Rechercher par titre");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        champRecherche.addValueChangeListener(
        e -> listDocuments(filter.getValue())
);
        filter.addValueChangeListener(e -> listDocuments(e.getValue()));
        typeRecherche.addValueChangeListener(
                e -> listDocuments(filter.getValue())
        );

        // ------------------------------------------------------------------
        // Sélection d'une ligne → ouvre l'éditeur
        // ------------------------------------------------------------------
        grid.asSingleSelect().addValueChangeListener(e -> editor.editDocument(e.getValue()));

        // Bouton "Nouveau" — passe un DTO vide (comme AuteurView passe un AuteurResponseDto vide)
        addNewBtn.addClickListener(e -> editor.editDocument(new DocumentResponseDto()));

        // Rafraîchit la liste après une action dans l'éditeur
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listDocuments(filter.getValue());
        });

        // Chargement initial
        listDocuments(null);
    }

    // ------------------------------------------------------------------
    // Délègue au service selon qu'un filtre est actif ou non
    // ------------------------------------------------------------------
    void listDocuments(String filterText) {

                if (!StringUtils.hasText(filterText)) {
                        grid.setItems(documentService.getAll());
                        return;
                }

                ChampRechercheDocument champ =
                        champRecherche.getValue() == null
                                ? ChampRechercheDocument.TITRE
                                : champRecherche.getValue();

                TypeRecherche type =
                        typeRecherche.getValue() == null
                                ? TypeRecherche.CONTIENT
                                : typeRecherche.getValue();

                grid.setItems(
                        documentService.searchDocuments(
                                champ,
                                type,
                                filterText
                        )
                );
        }
}