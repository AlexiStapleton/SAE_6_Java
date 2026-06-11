package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.LivreService;
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
import org.springframework.util.StringUtils;

@Route (value="livre")
@PageTitle("Les Livres")
@Menu(title = "Les Livres", order = 1, icon = "vaadin:clipboard-check")

public class LivreView extends VerticalLayout {

	private final LivreService livreService;

	final Grid<LivreResponseDto> grid;

	final TextField filter;

	private final Button addNewBtn;

	public LivreView(LivreService livreService, LivreEditor editor) {

		this.livreService = livreService;

		this.grid = new Grid<>(LivreResponseDto.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Ajouter un livre", VaadinIcon.PLUS.create());

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		add(actions, grid, editor);

		grid.setHeight("300px");
		grid.setColumns("id", "titre");

		// Add the custom column for auteur description
		grid.addColumn(livre -> {
			String nomAuteur = livre.getNomAuteur();
			return nomAuteur != null ? nomAuteur : "";
		}).setHeader("Auteur").setKey("nomAuteur");
		grid.addColumns("datePublication", "nomEditeur", "nbPages");


		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		filter.setPlaceholder("Filtrer par titre");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listLivres(e.getValue()));


		// Connect selected Livre to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editLivre(livreService.getLivreById(e.getValue().getId()));
		});

		// Instantiate and edit new Livre when the new button is clicked
		addNewBtn.addClickListener(e -> editor.editLivre(new LivreDetailResponseDto()));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listLivres(filter.getValue());
		});

		// Initialize listing
		listLivres(null);
	}

	void listLivres(String filterText) {
		if (StringUtils.hasText(filterText)) {
			grid.setItems(livreService.getByTitreContainingIgnoreCase(filterText));
		} else {
			grid.setItems(livreService.getAllLivres());
		}
	}

}
