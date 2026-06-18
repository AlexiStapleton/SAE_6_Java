package com.usmb.but3.td4biblio.view.emprunt;

import com.usmb.but3.td4biblio.dto.*;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class AddEmpruntDialog extends Dialog {

    @FunctionalInterface
    interface SaveCallback {
        EmpruntDetailResponseDto save(EmpruntCreateDto dto);
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }

    private final ComboBox<UtilisateurResponseDto>  utilisateurBox;
    private final ComboBox<DocumentResponseDto> documentBox;
    private final SaveCallback  saveCallback;
    private final ErrorCallback errorCallback;

    AddEmpruntDialog(
            UtilisateurService utilisateurService,
            DocumentService    documentService,
            EmpruntService empruntService,
            SaveCallback       saveCallback,
            ErrorCallback      errorCallback) {
        this.saveCallback  = saveCallback;
        this.errorCallback = errorCallback;

        setHeaderTitle("Créer un emprunt");

        var idsEmpruntes = empruntService.getDocumentIdsActuellementEmpruntes();


        utilisateurBox = new ComboBox<>("Emprunteur");
        var idsLimiteAtteinte = empruntService.getUtilisateurIdsAyantAtteintLimite();

        utilisateurBox.setItems(utilisateurService.getAll().stream()
                .filter(u -> !idsLimiteAtteinte.contains(u.getId()))
                .filter(u -> u.getDateFinAbonnement() != null
                        && !u.getDateFinAbonnement().isBefore(LocalDate.now()))
                .toList());
        utilisateurBox.setItemLabelGenerator(u ->
                u.getNom() + " " + u.getPrenom() + " — carte n°" + u.getNumeroCarte());
        utilisateurBox.setWidthFull();

        var documentsDisponibles = documentService.getAll().stream()
                .filter(d -> !idsEmpruntes.contains(d.getId()))
                .toList();

        documentBox = new ComboBox<>("Document");
        documentBox.setItems(documentsDisponibles);
        documentBox.setItemLabelGenerator(DocumentResponseDto::getTitre);
        documentBox.setWidthFull();

        var dateFinLabel = new Span("Date de retour prévue : " +
                LocalDate.now().plusWeeks(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateFinLabel.getStyle().set("color", "var(--lumo-secondary-text-color)");
        dateFinLabel.getStyle().set("font-size", "var(--lumo-font-size-s)");


        var layout = new VerticalLayout(utilisateurBox, documentBox, dateFinLabel);
        layout.setPadding(false);
        add(layout);

        var saveButton   = new Button("Enregistrer", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        var cancelButton = new Button("Annuler", e -> close());
        getFooter().add(cancelButton, saveButton);
    }

    private void save() {
        if (utilisateurBox.isEmpty()) {
            showWarning("Veuillez sélectionner un emprunteur.");
            return;
        }
        if (documentBox.isEmpty()) {
            showWarning("Veuillez sélectionner un document.");
            return;
        }

        var dto = new EmpruntCreateDto();
        dto.setUtilisateurId(utilisateurBox.getValue().getId());
        dto.setDocumentId(documentBox.getValue().getId());

        try {
            saveCallback.save(dto);
            close();
        } catch (RuntimeException e) {
            errorCallback.handleException(e);
        }
    }

    private void showWarning(String message) {
        var n = new Notification(message);
        n.setPosition(Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_WARNING);
        n.setDuration(3000);
        n.open();
    }
}