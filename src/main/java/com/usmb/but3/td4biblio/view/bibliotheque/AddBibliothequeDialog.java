package com.usmb.but3.td4biblio.view.bibliotheque;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;

class AddBibliothequeDialog extends Dialog {

    @FunctionalInterface
    interface SaveCallback {
        /**
         * Persiste la bibliothèque et retourne le détail sauvegardé.
         * Le retour permet à l'appelant (ex: AddEvenementDialog)
         * de rafraîchir sa ComboBox et de pré-sélectionner la nouvelle entrée.
         */
        BibliothequeDetailResponseDto save(BibliothequeCreateDto dto);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }

    private final SaveCallback saveCallback;
    private final ErrorCallback errorCallback;
    private final BibliothequeForm form;

    AddBibliothequeDialog(
            List<Adresse> adresses,
            SaveCallback saveCallback,
            ErrorCallback errorCallback) {
        this.saveCallback  = saveCallback;
        this.errorCallback = errorCallback;

        form = new BibliothequeForm(adresses);
        form.setFormDataObject(new BibliothequeCreateDto());

        var saveButton   = new Button("Enregistrer", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        var cancelButton = new Button("Annuler", e -> close());

        setHeaderTitle("Ajouter une bibliothèque");
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