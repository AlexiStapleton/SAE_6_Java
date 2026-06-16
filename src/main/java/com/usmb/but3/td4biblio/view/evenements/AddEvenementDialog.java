package com.usmb.but3.td4biblio.view.evenements;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.view.evenements.EvenementForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;

class AddEvenementDialog extends Dialog {

    @FunctionalInterface
    interface SaveCallback {
        void save(EvenementCreateDto dto);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }

    private final SaveCallback saveCallback;
    private final ErrorCallback errorCallback;
    private final EvenementForm form;

    AddEvenementDialog(
            List<Bibliotheque> bibliotheques,
            List<TypeEvenement> typeEvenements,
            SaveCallback saveCallback,
            ErrorCallback errorCallback) {
        this.saveCallback  = saveCallback;
        this.errorCallback = errorCallback;

        form = new EvenementForm(bibliotheques, typeEvenements);
        form.setFormDataObject(new EvenementCreateDto());

        var saveButton   = new Button("Enregistrer", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        var cancelButton = new Button("Annuler", e -> close());

        setHeaderTitle("Ajouter un événement");
        add(form);
        getFooter().add(cancelButton, saveButton);
    }

    private void save() {
        form.getFormDataObject().ifPresent(dto -> {
            try {
                saveCallback.save(dto);
                close();
            } catch (RuntimeException e) {
                errorCallback.handleException(e);
            }
        });
    }
}