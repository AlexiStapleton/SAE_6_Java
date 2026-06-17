package com.usmb.but3.td4biblio.view.emprunt;

import com.usmb.but3.td4biblio.dto.*;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.usmb.but3.td4biblio.view.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@PageTitle("Emprunts")
@Route(value = "emprunts", layout = MainLayout.class)
@Menu(title = "Les Emprunts", order = 3, icon = "vaadin:exchange")
public class EmpruntView extends HorizontalLayout {

    private final EmpruntService     empruntService;
    private final UtilisateurService utilisateurService;
    private final DocumentService    documentService;
    private final boolean            isBibliothecaire;

    private final Grid<EmpruntResponseDto> grid;
    private final EmpruntFormDrawer        drawer;
    private final TextField                searchField;

    public EmpruntView(
            EmpruntService empruntService,
            UtilisateurService utilisateurService,
            DocumentService documentService,
            AuthenticationContext authContext) {
        this.empruntService     = empruntService;
        this.utilisateurService = utilisateurService;
        this.documentService    = documentService;
        this.isBibliothecaire   = authContext.hasRole("BIBLIOTHECAIRE");

        // --- Recherche ---
        searchField = new TextField();
        searchField.setPlaceholder("Rechercher par emprunteur…");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> refreshGrid(e.getValue()));

        // --- Grid ---
        grid = new Grid<>();
        grid.addColumn(dto -> dto.getId() != null ? dto.getId().getUtilisateurId() : "")
                .setHeader("ID Utilisateur").setWidth("120px").setFlexGrow(0);
        grid.addColumn(dto -> dto.getId() != null ? dto.getId().getDocumentId() : "")
                .setHeader("ID Document").setWidth("110px").setFlexGrow(0);
        grid.addColumn(EmpruntResponseDto::getNomUtilisateur).setHeader("Emprunteur").setSortProperty("nomUtilisateur");
        grid.addColumn(EmpruntResponseDto::getNomDocument).setHeader("Document").setSortProperty("nomDocument");
        grid.addColumn(EmpruntResponseDto::getDateCreation).setHeader("Date de création");
        grid.addColumn(EmpruntResponseDto::getProlongation).setHeader("Prolongation");

        // --- Drawer ---
        drawer = new EmpruntFormDrawer(
                isBibliothecaire,
                (emprunt, nouvelleProlongation) -> {
                    var dto = new EmpruntCreateDto();
                    dto.setUtilisateurId(emprunt.getId().getUtilisateurId());
                    dto.setDocumentId(emprunt.getId().getDocumentId());
                    dto.setProlongation(nouvelleProlongation);

                    var updated = empruntService.update(emprunt.getId(), dto);
                    refreshGrid(searchField.getValue());
                    return updated;
                },
                this::handleException
        );

        grid.addSelectionListener(e ->
                e.getFirstSelectedItem().ifPresentOrElse(
                        dto -> {
                            try {
                                var detail = empruntService.getById(dto.getId());
                                drawer.setEmpruntDetail(detail);
                            } catch (RessourceNotFoundException ex) {
                                handleException(ex);
                            }
                        },
                        () -> drawer.setEmpruntDetail(null)
                )
        );

        // --- Toolbar ---
        var toolbar = new HorizontalLayout(searchField);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.expand(searchField);
        toolbar.setWidthFull();

        if (isBibliothecaire) {
            var addButton = new Button("Créer un emprunt", VaadinIcon.PLUS.create(), e ->
                    new AddEmpruntDialog(
                            utilisateurService,
                            documentService,
                            dto -> {
                                var saved = empruntService.create(dto);
                                refreshGrid(searchField.getValue());
                                return saved;
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

        refreshGrid(null);
    }

    private void refreshGrid(String filterText) {
        List<EmpruntResponseDto> items = buildItemList(filterText);
        grid.setItems(items);
    }

    private List<EmpruntResponseDto> buildItemList(String filterText) {
        var all = empruntService.getAll();
        if (filterText == null || filterText.isBlank()) return all;
        String lower = filterText.toLowerCase();
        return all.stream()
                .filter(e -> e.getNomUtilisateur() != null
                        && e.getNomUtilisateur().toLowerCase().contains(lower))
                .toList();
    }

    private void handleException(RuntimeException exception) {
        if (exception instanceof RessourceNotFoundException) {
            showWarning("Ressource introuvable : " + exception.getMessage());
        } else if (exception instanceof AccessDeniedException) {
            showWarning("Action non autorisée : vous devez être bibliothécaire.");
        } else if (exception instanceof org.springframework.dao.DataIntegrityViolationException) {
            showWarning("Cet emprunt existe déjà ou les données sont invalides.");
        } else if (exception instanceof IllegalStateException) {
            showWarning(exception.getMessage());
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
}