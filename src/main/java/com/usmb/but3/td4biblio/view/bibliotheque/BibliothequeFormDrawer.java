package com.usmb.but3.td4biblio.view.bibliotheque;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.Nullable;

import java.util.List;

class BibliothequeFormDrawer extends Composite<VerticalLayout> {

    @FunctionalInterface
    interface SaveCallback {
        BibliothequeDetailResponseDto save(Integer id, BibliothequeCreateDto dto);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }

    private final SaveCallback saveCallback;
    private final ErrorCallback errorCallback;
    private final BibliothequeForm form;
    private Integer currentId;

    BibliothequeFormDrawer(
            List<Adresse> adresses,
            boolean readOnly,
            SaveCallback saveCallback,
            ErrorCallback errorCallback) {
        this.saveCallback  = saveCallback;
        this.errorCallback = errorCallback;

        form = new BibliothequeForm(adresses);
        form.setReadOnly(readOnly);

        var header = new H2("Détail de la bibliothèque");
        var layout = getContent();
        layout.add(header, new Scroller(form));

        if (!readOnly) {
            var saveButton = new Button("Enregistrer", e -> save());
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(saveButton);
        }

        layout.setWidth("320px");
        addClassName(LumoUtility.BoxShadow.MEDIUM);
        setVisible(false);
    }

    void setBibliothequeDetail(@Nullable BibliothequeDetailResponseDto detail) {
        if (detail == null) {
            currentId = null;
            form.setFormDataObject(null);
            setVisible(false);
            return;
        }

        currentId = detail.getId();

        var dto = new BibliothequeCreateDto();
        dto.setNom(detail.getNom());
        if (detail.getAdresse() != null) {
            dto.setAdresseId(detail.getAdresse().getId());
        }

        form.setFormDataObject(dto);
        setVisible(true);
    }

    private void save() {
        if (currentId == null) return;
        form.getFormDataObject().ifPresent(dto -> {
            try {
                var saved = saveCallback.save(currentId, dto);
                setBibliothequeDetail(saved);
            } catch (RuntimeException e) {
                errorCallback.handleException(e);
            }
        });
    }
}