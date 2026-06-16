package com.usmb.but3.td4biblio.view.evenements;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

class EvenementForm extends Composite<FormLayout> {

    private final Binder<EvenementCreateDto> binder;
    private final List<Bibliotheque> bibliotheques;
    private final List<TypeEvenement> typeEvenements;

    // Champs gardés en instance pour pouvoir appeler setReadOnly(true)
    private final TextField nomField;
    private final DatePicker dateDebutField;
    private final DatePicker dateFinField;
    private final ComboBox<Bibliotheque> bibliothequeBox;
    private final ComboBox<TypeEvenement> typeEvenementBox;

    EvenementForm(List<Bibliotheque> bibliotheques, List<TypeEvenement> typeEvenements) {
        this.bibliotheques  = bibliotheques;
        this.typeEvenements = typeEvenements;

        nomField          = new TextField("Nom");
        dateDebutField    = new DatePicker("Date de début");
        dateFinField      = new DatePicker("Date de fin");

        bibliothequeBox = new ComboBox<>("Bibliothèque");
        bibliothequeBox.setItems(bibliotheques);
        bibliothequeBox.setItemLabelGenerator(Bibliotheque::getNom);

        typeEvenementBox = new ComboBox<>("Type d'événement");
        typeEvenementBox.setItems(typeEvenements);
        typeEvenementBox.setItemLabelGenerator(TypeEvenement::getNom);

        var layout = getContent();
        layout.add(nomField, dateDebutField, dateFinField,
                bibliothequeBox, typeEvenementBox);

        binder = new Binder<>();

        binder.forField(nomField)
                .asRequired("Saisir un nom")
                .bind(EvenementCreateDto::getNom, EvenementCreateDto::setNom);

        binder.forField(dateDebutField)
                .asRequired("Saisir une date de début")
                .bind(EvenementCreateDto::getDateDebut, EvenementCreateDto::setDateDebut);

        binder.forField(dateFinField)
                .withValidator(
                        dateFin -> dateFin == null
                                || dateDebutField.getValue() == null
                                || !dateFin.isBefore(dateDebutField.getValue()),
                        "La date de fin ne peut pas être antérieure à la date de début"
                )
                .bind(EvenementCreateDto::getDateFin, EvenementCreateDto::setDateFin);

        // Revalide dateFin à chaque changement de dateDebut
        dateDebutField.addValueChangeListener(e -> binder.validate());

        binder.forField(bibliothequeBox)
                .asRequired("Sélectionner une bibliothèque")
                .bind(
                        dto -> bibliotheques.stream()
                                .filter(b -> b.getId().equals(dto.getBibliothequeId()))
                                .findFirst().orElse(null),
                        (dto, b) -> dto.setBibliothequeId(b == null ? null : b.getId())
                );

        binder.forField(typeEvenementBox)
                .asRequired("Sélectionner un type d'événement")
                .bind(
                        dto -> typeEvenements.stream()
                                .filter(t -> t.getId().equals(dto.getTypeEvenementId()))
                                .findFirst().orElse(null),
                        (dto, t) -> dto.setTypeEvenementId(t == null ? null : t.getId())
                );
    }

    /**
     * Passe tous les champs en lecture seule (pour les EMPRUNTEUR).
     * Les valeurs restent visibles mais non modifiables.
     */
    void setReadOnly(boolean readOnly) {
        nomField.setReadOnly(readOnly);
        dateDebutField.setReadOnly(readOnly);
        dateFinField.setReadOnly(readOnly);
        bibliothequeBox.setReadOnly(readOnly);
        typeEvenementBox.setReadOnly(readOnly);
    }

    void setFormDataObject(@Nullable EvenementCreateDto dto) {
        binder.setBean(dto);
    }

    Optional<EvenementCreateDto> getFormDataObject() {
        if (binder.getBean() == null) {
            throw new IllegalStateException("Pas d'objet de formulaire");
        }
        return binder.validate().isOk()
                ? Optional.of(binder.getBean())
                : Optional.empty();
    }
}