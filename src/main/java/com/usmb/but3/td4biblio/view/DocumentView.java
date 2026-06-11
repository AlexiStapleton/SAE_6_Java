package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;

import org.springframework.context.annotation.Scope;

@Component
@Scope("prototype")
@Route(value = "documents", layout = MainLayout.class)
@PageTitle("Documents")
@Menu(
    title = "Documents",
    order = 4,
    icon = "vaadin:file-text"
)
public class DocumentView extends VerticalLayout {

    private final DocumentService documentService;

    private final DocumentEditor editor;

    final Grid<Document> grid =
            new Grid<>(Document.class);

    final TextField filter =
            new TextField();

    private final Button addNewBtn =
            new Button(
                    "Nouveau document",
                    VaadinIcon.PLUS.create()
            );

    public DocumentView(
            DocumentService documentService,
            DocumentEditor editor) {

        this.documentService = documentService;
        this.editor = editor;

        filter.setPlaceholder(
                "Rechercher par titre");

        filter.setClearButtonVisible(true);

        filter.addValueChangeListener(
                e -> listDocuments(
                        e.getValue()));

        addNewBtn.addClickListener(
                e -> editor.editDocument(
                        new Document()));

        grid.setColumns(
                "id",
                "titre",
                "format",
                "datePublication",
                "empruntable"
        );

        grid.getColumnByKey("id")
                .setWidth("80px")
                .setFlexGrow(0);

        grid.asSingleSelect()
                .addValueChangeListener(
                        e -> editor.editDocument(
                                e.getValue()));

        editor.setChangeHandler(() -> {

            editor.setVisible(false);

            listDocuments(filter.getValue());
        });

        HorizontalLayout actions =
                new HorizontalLayout(
                        filter,
                        addNewBtn);

        add(actions, grid, editor);

        listDocuments(null);
    }

    void listDocuments(String filterText) {

        if(!StringUtils.hasText(filterText)) {

            grid.setItems(
                    documentService.getAllDocuments());
        }
        else {

            grid.setItems(
                    documentService.searchByTitre(
                            filterText));
        }
    }
}