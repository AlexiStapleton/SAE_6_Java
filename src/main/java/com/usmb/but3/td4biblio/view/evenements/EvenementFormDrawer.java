package com.usmb.but3.td4biblio.view.evenements;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.dto.EvenementDetailResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.Nullable;

import java.util.List;

class EvenementFormDrawer extends Composite<VerticalLayout> {

    @FunctionalInterface
    interface SaveCallback {
        EvenementDetailResponseDto save(Integer id, EvenementCreateDto dto);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }

    private final SaveCallback saveCallback;
    private final ErrorCallback errorCallback;
    private final EvenementForm form;
    private Integer currentId;

    /**
     * @param readOnly si true, les champs sont en lecture seule
     *                 et le bouton Enregistrer est masqué.
     */
    EvenementFormDrawer(
            List<Bibliotheque> bibliotheques,
            List<TypeEvenement> typeEvenements,
            boolean readOnly,
            SaveCallback saveCallback,
            ErrorCallback errorCallback) {
        this.saveCallback  = saveCallback;
        this.errorCallback = errorCallback;

        form = new EvenementForm(bibliotheques, typeEvenements);
        form.setReadOnly(readOnly);

        var header = new H2("Détail de l'événement");
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

    void setEvenementDetail(@Nullable EvenementDetailResponseDto detail) {
        if (detail == null) {
            currentId = null;
            form.setFormDataObject(null);
            setVisible(false);
            return;
        }

        currentId = detail.getId();

        var dto = new EvenementCreateDto();
        dto.setNom(detail.getNom());
        dto.setDateDebut(detail.getDateDebut());
        dto.setDateFin(detail.getDateFin());
        if (detail.getBibliotheque() != null) {
            dto.setBibliothequeId(detail.getBibliotheque().getId());
        }
        if (detail.getTypeEvenement() != null) {
            dto.setTypeEvenementId(detail.getTypeEvenement().getId());
        }

        form.setFormDataObject(dto);
        setVisible(true);
    }

    private void save() {
        if (currentId == null) return;
        form.getFormDataObject().ifPresent(dto -> {
            try {
                var saved = saveCallback.save(currentId, dto);
                setEvenementDetail(saved);
            } catch (RuntimeException e) {
                errorCallback.handleException(e);
            }
        });
    }
}