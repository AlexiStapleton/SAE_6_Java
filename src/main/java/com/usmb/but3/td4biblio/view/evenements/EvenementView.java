package com.usmb.but3.td4biblio.view.evenements;

import com.usmb.but3.td4biblio.dto.EvenementResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.service.EvenementService;
import com.usmb.but3.td4biblio.view.MainLayout;
import com.usmb.but3.td4biblio.view.evenements.AddEvenementDialog;
import com.usmb.but3.td4biblio.view.evenements.EvenementFormDrawer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

@PageTitle("Événements")
@Route(value = "evenements", layout = MainLayout.class)
@Menu(title = "Les événements", order = 10, icon = "vaadin:clipboard-check")
class EvenementView extends HorizontalLayout
        implements HasUrlParameter<Integer> {

    private final EvenementService service;
    private final Grid<EvenementResponseDto> grid;
    private final EvenementFormDrawer drawer;
    private final boolean isBibliothecaire;

    private final List<Bibliotheque> bibliotheques;
    private final List<TypeEvenement> typeEvenements;

    EvenementView(EvenementService service,
                         com.usmb.but3.td4biblio.repository.BibliothequeRepo bibliothequeRepo,
                         com.usmb.but3.td4biblio.repository.TypeEvenementRepo typeEvenementRepo,
                         AuthenticationContext authContext) {
        this.service = service;

        // Vérifie le rôle une seule fois à la construction de la vue
        this.isBibliothecaire = authContext.hasRole("BIBLIOTHECAIRE");

        this.bibliotheques  = bibliothequeRepo.findAll();
        this.typeEvenements = typeEvenementRepo.findAll();

        // --- Barre de recherche ---
        var searchField = new TextField();
        searchField.setPlaceholder("Rechercher par nom…");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

        // --- Grid ---
        grid = new Grid<>();
        grid.addColumn(EvenementResponseDto::getNom)
                .setHeader("Nom")
                .setSortProperty("nom");
        grid.addColumn(EvenementResponseDto::getDateDebut)
                .setHeader("Date de début")
                .setSortProperty("dateDebut");
        grid.addColumn(EvenementResponseDto::getDateFin)
                .setHeader("Date de fin")
                .setSortProperty("dateFin");
        grid.addColumn(EvenementResponseDto::getNomBibliotheque)
                .setHeader("Bibliothèque")
                .setSortProperty("nomBibliotheque");
        grid.addColumn(EvenementResponseDto::getNomTypeEvenement)
                .setHeader("Type d'événement")
                .setSortProperty("nomTypeEvenement");

        grid.setItemsPageable(pageable ->
                service.findPaginated(searchField.getValue(), pageable).getContent());

        // --- Drawer : lecture seule pour EMPRUNTEUR, éditable pour BIBLIOTHECAIRE ---
        drawer = new EvenementFormDrawer(
                bibliotheques,
                typeEvenements,
                !isBibliothecaire, // readOnly = true pour les EMPRUNTEUR
                (id, dto) -> {
                    var saved = service.update(id, dto);
                    grid.getDataProvider().refreshAll();
                    return saved;
                },
                this::handleException
        );

        searchField.addValueChangeListener(e -> grid.getDataProvider().refreshAll());

        // Sélection d'une ligne → navigation par URL
        grid.addSelectionListener(e ->
                e.getFirstSelectedItem()
                        .map(EvenementResponseDto::getId)
                        .ifPresentOrElse(
                                EvenementView::showEvenementDetail,
                                EvenementView::showCatalog
                        )
        );

        // --- Toolbar ---
        var toolbar = new HorizontalLayout(searchField);
        toolbar.setWidthFull();
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.expand(searchField);

        // Bouton Ajouter — visible seulement pour BIBLIOTHECAIRE
        if (isBibliothecaire) {
            var addButton = new Button("Ajouter un événement", e ->
                    new AddEvenementDialog(
                            bibliotheques,
                            typeEvenements,
                            dto -> {
                                var saved = service.create(dto);
                                grid.getDataProvider().refreshAll();
                                showEvenementDetail(saved.getId());
                            },
                            this::handleException
                    ).open()
            );
            toolbar.add(addButton);
        }

        // --- Layout ---
        setSizeFull();
        setSpacing(false);

        var listLayout = new VerticalLayout(toolbar, grid);
        listLayout.setSizeFull();
        grid.setSizeFull();

        add(listLayout, drawer);
        setFlexShrink(0, drawer);
    }

    // --- Gestion des exceptions ---

    private void handleException(RuntimeException exception) {
        if (exception instanceof RessourceNotFoundException) {
            showWarning("Ressource introuvable : " + exception.getMessage());
        } else if (exception instanceof AccessDeniedException) {
            showWarning("Action non autorisée : vous devez être bibliothécaire.");
        } else if (exception instanceof org.springframework.dao.DataIntegrityViolationException) {
            showWarning("Violation de contrainte : vérifiez les données saisies.");
        } else if (exception instanceof org.springframework.orm.ObjectOptimisticLockingFailureException) {
            showWarning("Conflit de modification : un autre utilisateur a modifié cet événement. Veuillez rafraîchir.");
        } else {
            throw exception;
        }
    }

    private void showWarning(String message) {
        var n = new Notification(message);
        n.setPosition(Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_WARNING);
        n.setDuration(4000);
        n.open();
    }

    // --- Navigation par URL ---

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer evenementId) {
        if (evenementId != null) {
            grid.getDataProvider().fetch(
                            new com.vaadin.flow.data.provider.Query<>()
                    ).filter(dto -> dto.getId().equals(evenementId))
                    .findFirst()
                    .ifPresent(grid::select);
        } else {
            grid.deselectAll();
        }

        // Le drawer s'ouvre pour tous, en lecture seule pour les EMPRUNTEUR
        drawer.setEvenementDetail(
                Optional.ofNullable(evenementId)
                        .map(id -> {
                            try {
                                return service.getById(id);
                            } catch (RessourceNotFoundException e) {
                                return null;
                            }
                        })
                        .orElse(null)
        );
    }

    public static void showEvenementDetail(Integer evenementId) {
        UI.getCurrent().navigate(EvenementView.class, evenementId);
    }

    public static void showCatalog() {
        UI.getCurrent().navigate(EvenementView.class);
    }
}