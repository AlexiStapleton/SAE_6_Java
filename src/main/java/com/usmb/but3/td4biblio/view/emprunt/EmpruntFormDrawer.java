package com.usmb.but3.td4biblio.view.emprunt;

import com.usmb.but3.td4biblio.dto.EmpruntDetailResponseDto;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

class EmpruntFormDrawer extends Composite<VerticalLayout> {

    @FunctionalInterface
    interface ProlongerCallback {
        EmpruntDetailResponseDto prolonger(EmpruntDetailResponseDto emprunt); // plus de LocalDate
    }

    @FunctionalInterface
    interface ErrorCallback {
        void handleException(RuntimeException e);
    }

    private final ProlongerCallback prolongerCallback;
    private final ErrorCallback     errorCallback;
    private final boolean           isBibliothecaire;

    private final Span      emprunteurLabel   = new Span();
    private final Span      documentLabel     = new Span();
    private final Span      dateCreationLabel = new Span();
    private final Span      prolongationLabel = new Span();
    private final Button     prolongerButton;
    private final Span dateFinLabel = new Span();

    private EmpruntDetailResponseDto currentEmprunt;

    EmpruntFormDrawer(
            boolean isBibliothecaire,
            ProlongerCallback prolongerCallback,
            ErrorCallback errorCallback) {
        this.isBibliothecaire  = isBibliothecaire;
        this.prolongerCallback = prolongerCallback;
        this.errorCallback     = errorCallback;

        var form = new FormLayout();
        form.addFormItem(emprunteurLabel,   "Emprunteur");
        form.addFormItem(documentLabel,     "Document");
        form.addFormItem(dateCreationLabel, "Date de création");
        form.addFormItem(dateFinLabel, "Date de fin");
        form.addFormItem(prolongationLabel, "Prolongation actuelle");

         prolongerButton = new Button("Prolonger de 5 semaines", e -> prolonger());
        prolongerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var layout = getContent();
        layout.add(new H2("Détail de l'emprunt"), form);

        // Les emprunteurs peuvent prolonger une fois, les bibliothécaires sans limite
        layout.add(prolongerButton);

        layout.setWidth("360px");
        addClassName(LumoUtility.BoxShadow.MEDIUM);
        setVisible(false);
    }

    void setEmpruntDetail(@Nullable EmpruntDetailResponseDto detail) {
        if (detail == null) {
            currentEmprunt = null;
            setVisible(false);
            return;
        }

        currentEmprunt = detail;

        emprunteurLabel.setText(
                detail.getUtilisateur() != null
                        ? detail.getUtilisateur().getNom() + " " + detail.getUtilisateur().getPrenom()
                        : "—");
        documentLabel.setText(
                detail.getDocument() != null
                        ? detail.getDocument().getTitre()
                        : "—");
        dateCreationLabel.setText(
                detail.getDateCreation() != null
                        ? detail.getDateCreation().toString()
                        : "—");
        dateFinLabel.setText(
                detail.getDateFin() != null
                        ? detail.getDateFin().toString()
                        : "—");
        prolongationLabel.setText(
                detail.getProlongation() != null
                        ? detail.getProlongation().toString()
                        : "Aucune");

        // Un emprunteur ne peut prolonger qu'une fois (si prolongation déjà définie, on désactive)
        boolean peutProlonger = isBibliothecaire || detail.getProlongation() == null;

        prolongerButton.setVisible(peutProlonger);



        setVisible(true);
    }

    private void prolonger() {
        if (currentEmprunt == null) return;
        try {
            var updated = prolongerCallback.prolonger(currentEmprunt);
            setEmpruntDetail(updated);
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