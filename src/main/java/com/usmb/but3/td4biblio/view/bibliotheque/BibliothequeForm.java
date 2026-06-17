package com.usmb.but3.td4biblio.view.bibliotheque;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

class BibliothequeForm extends Composite<FormLayout> {

    private final Binder<BibliothequeCreateDto> binder;
    private final List<Adresse> adresses;

    private final TextField nomField;
    private final ComboBox<Adresse> adresseBox;

    BibliothequeForm(List<Adresse> adresses) {
        this.adresses = adresses;

        nomField  = new TextField("Nom");

        adresseBox = new ComboBox<>("Adresse");
        adresseBox.setItems(adresses);
        // Label : "12 rue de la Paix — Annecy"
        adresseBox.setItemLabelGenerator(a ->
                a.getRue() + " — " + a.getVille());

        var layout = getContent();
        layout.add(nomField, adresseBox);

        binder = new Binder<>();

        binder.forField(nomField)
                .asRequired("Saisir un nom")
                .bind(BibliothequeCreateDto::getNom, BibliothequeCreateDto::setNom);

        binder.forField(adresseBox)
                .asRequired("Sélectionner une adresse")
                .bind(
                        dto -> adresses.stream()
                                .filter(a -> a.getId().equals(dto.getAdresseId()))
                                .findFirst().orElse(null),
                        (dto, a) -> dto.setAdresseId(a == null ? null : a.getId())
                );
    }

    void setReadOnly(boolean readOnly) {
        nomField.setReadOnly(readOnly);
        adresseBox.setReadOnly(readOnly);
    }

    void setFormDataObject(@Nullable BibliothequeCreateDto dto) {
        binder.setBean(dto);
    }

    Optional<BibliothequeCreateDto> getFormDataObject() {
        if (binder.getBean() == null) {
            throw new IllegalStateException("Pas d'objet de formulaire");
        }
        return binder.validate().isOk()
                ? Optional.of(binder.getBean())
                : Optional.empty();
    }
}