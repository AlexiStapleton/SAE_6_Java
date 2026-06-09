package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.LivreService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class LivreEditor extends VerticalLayout implements KeyNotifier {

    private final LivreService livreService;
	private final AuteurService auteurService;
	

	/**
	 * The currently edited livre
	 */
	private Livre livre;

	/* Fields to edit properties in Livre entity */
	TextField titre = new TextField("Titre");
	TextField editeur = new TextField("Editeur");
    DatePicker datePublication = new DatePicker("Date de publication");
    IntegerField nbPages = new IntegerField("Nombre de pages");
    {
        nbPages.setMin(1);
        nbPages.setMax(10000);
        nbPages.setStep(1);
        nbPages.setPlaceholder("Nombre de pages");
        nbPages.setClearButtonVisible(true);
    }

	ComboBox<Auteur> auteurComboBox = new ComboBox<>("Auteur");

	/* Action buttons */
	Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
	Button cancel = new Button("Annuler");
	Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<Livre> binder = new Binder<>(Livre.class);
	private ChangeHandler changeHandler;

	public LivreEditor(AuteurService auteurService, LivreService livreService) {

		this.livreService = livreService;
		this.auteurService = auteurService;

		auteurComboBox.setPlaceholder("Sélectionner un auteur");
		auteurComboBox.setClearButtonVisible(true);
		// do it after :
		//auteurComboBox.setItems(auteurService.getAllAuteurs());
		auteurComboBox.setItemLabelGenerator(Auteur::getDesc);

		add(titre, auteurComboBox, datePublication, editeur, nbPages , actions);

		// bind using naming convention
		binder.bindInstanceFields(this);
        binder.forField(auteurComboBox)
            .asRequired("Auteur est obligatoire")
            .bind(Livre::getAuteur, Livre::setAuteur);

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> editLivre(livre));
		setVisible(false);
	}

	void delete() {
		livreService.deleteLivreById(livre.getId());
		changeHandler.onChange();
	}

	void save() {
        if (livre.getId() == null) {
            // If the livre is new, we save it
            livreService.saveLivre(livre);
        } else {
            // If the livre already exists, we update it
            livreService.updateLivre(livre);
        }
        changeHandler.onChange();
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editLivre(Livre l) {
		if (l == null) {
			setVisible(false);
			return;
		}

		auteurComboBox.setItems(auteurService.getAllAuteurs());

		final boolean persisted = l.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			// In a more complex app, you might want to load
			// the entity/DTO with lazy loaded relations for editing
			livre = livreService.getLivreById(l.getId());
		}
		else {
			livre = l;
		}
		cancel.setVisible(persisted);

		// Bind livre properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(livre);

		setVisible(true);

		// Focus first name initially
		titre.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}

