package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;
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

import java.util.ArrayList;

// MS added for UI unit test : @Component and @Scope("prototype") are needed for the view to be instantiated correctly
@Component
@Scope("prototype")
@Route (value="auteur") 
@PageTitle("Les Auteurs")
@Menu(title = "Les Auteurs", order = 0, icon = "vaadin:clipboard-check")
public class AuteurView extends VerticalLayout {

	private final AuteurService auteurService;

	final Grid<Auteur> grid;

	final TextField filter;

	private final Button addNewBtn;

	public Button getAddNewBtn() {
		return addNewBtn;
	}

	final AuteurEditor editor;

	public AuteurView(AuteurService auteurService, AuteurEditor editor) {
		this.auteurService = auteurService;
		this.editor = editor;
		this.grid = new Grid<>(Auteur.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Ajouter un auteur", VaadinIcon.PLUS.create());

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		add(actions, grid, editor);

		grid.setHeight("300px");
		grid.setColumns("id", "nom", "prenom", "nationalite", "dateNaissance", "dateDeces");
		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		filter.setPlaceholder("Filtrer par nom");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listAuteurs(e.getValue()));

		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editAuteur(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editAuteur(new Auteur(null, "", "", "", null, null, new ArrayList<>())));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listAuteurs(filter.getValue());
		});

		// Initialize listing
		listAuteurs(null);
	}

	// tag::listAuteurs[]
	void listAuteurs(String filterText) {
		if (StringUtils.hasText(filterText)) {
			grid.setItems(auteurService.getByNomContainingIgnoreCase(filterText));
		} else {
			grid.setItems(auteurService.getAllAuteurs());
		}
	}
	// end::listCustomers[]

}
