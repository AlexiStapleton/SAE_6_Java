package com.usmb.but3.td4biblio.view.genreDocuments;

import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.service.GenreDocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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

import java.util.List;

/**
 * Vue principale de gestion des genres de documents.
 * Affiche la liste des genres avec filtrage, et permet la création,
 * modification et suppression via l'éditeur inline GenreDocumentEditor.
 * Architecture calquée sur DocumentView.
 */
@Component
@Scope("prototype")
@Route(value = "genres-documents")
@PageTitle("Genres de documents")
@Menu(title = "Genres", order = 2, icon = "vaadin:tag")
public class GenreDocumentView extends VerticalLayout {

    private final GenreDocumentService genreDocumentService;
    private final GenreDocumentEditor editor;

    final Grid<GenreDocumentResponseDto> grid;
    final TextField filter;
    private final Button addNewBtn;

    public GenreDocumentView(
            GenreDocumentService genreDocumentService,
            GenreDocumentEditor editor
    ) {
        this.genreDocumentService = genreDocumentService;
        this.editor = editor;

        this.grid      = new Grid<>(GenreDocumentResponseDto.class);
        this.filter    = new TextField();
        this.addNewBtn = new Button("Nouveau genre", VaadinIcon.PLUS.create());

        // ------------------------------------------------------------------
        // Layout
        // ------------------------------------------------------------------
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        // ------------------------------------------------------------------
        // Grille
        // ------------------------------------------------------------------
        grid.setHeight("400px");
        grid.setColumns("id", "nom");

        grid.getColumnByKey("id").setWidth("80px").setFlexGrow(0);
        grid.getColumnByKey("nom").setHeader("Nom du genre");

        grid.addComponentColumn(genre -> {

            Button modifier = new Button(VaadinIcon.EDIT.create());
            Button supprimer = new Button(VaadinIcon.TRASH.create());

            modifier.addClickListener(e -> editor.editGenre(genre));

            supprimer.addClickListener(e -> openDeleteDialog(genre));

            return new HorizontalLayout(modifier, supprimer);

        }).setHeader("Actions");

        // ------------------------------------------------------------------
        // Filtre
        // ------------------------------------------------------------------
        filter.setPlaceholder("Rechercher par nom");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listGenres(e.getValue()));

        // ------------------------------------------------------------------
        // Sélection d'une ligne → ouvre l'éditeur
        // ------------------------------------------------------------------
        grid.asSingleSelect().addValueChangeListener(
                e -> editor.editGenre(e.getValue())
        );

        // ------------------------------------------------------------------
        // Bouton "Nouveau"
        // ------------------------------------------------------------------
        addNewBtn.addClickListener(e -> {
            // DTO vide avec Id null → l'éditeur sait que c'est une création
            grid.asSingleSelect().clear();
            editor.editGenre(new GenreDocumentResponseDto());
        });

        // ------------------------------------------------------------------
        // Rafraîchissement après action dans l'éditeur
        // ------------------------------------------------------------------
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listGenres(filter.getValue());
        });

        // ------------------------------------------------------------------
        // Chargement initial
        // ------------------------------------------------------------------
        listGenres(null);
    }

    // ------------------------------------------------------------------
    // Chargement de la liste avec filtre optionnel côté client
    // ------------------------------------------------------------------
    void listGenres(String filterText) {
        List<GenreDocumentResponseDto> genres = genreDocumentService.getAll();

        if (StringUtils.hasText(filterText)) {
            String lower = filterText.toLowerCase();
            genres = genres.stream()
                    .filter(g -> g.getNom() != null
                            && g.getNom().toLowerCase().contains(lower))
                    .toList();
        }

        grid.setItems(genres);
    }

    // ------------------------------------------------------------------
    // Dialog de confirmation de suppression
    // ------------------------------------------------------------------
    private void openDeleteDialog(GenreDocumentResponseDto genre) {
        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setHeader("Suppression");
        dialog.setText("Supprimer le genre « " + genre.getNom() + " » ?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Supprimer");

        dialog.addConfirmListener(e -> {
            genreDocumentService.delete(genre.getId());
            listGenres(filter.getValue());
        });

        dialog.open();
    }
}