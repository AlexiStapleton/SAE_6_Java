package com.usmb.but3.td4biblio.view.bibliotheque;

import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.usmb.but3.td4biblio.view.MainLayout;
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

@PageTitle("Bibliothèques")
@Route(value = "bibliotheques", layout = MainLayout.class)
@Menu(title = "Les Bibliothèques", order = 10, icon = "vaadin:clipboard-check")
class BibliothequeView extends HorizontalLayout
        implements HasUrlParameter<Integer> {

    private final BibliothequeService service;
    private final Grid<BibliothequeResponseDto> grid;
    private final BibliothequeFormDrawer drawer;
    private final boolean isBibliothecaire;
    private final List<Adresse> adresses;

    BibliothequeView(BibliothequeService service,
                            AdresseRepo adresseRepo,
                            AuthenticationContext authContext) {
        this.service          = service;
        this.isBibliothecaire = authContext.hasRole("BIBLIOTHECAIRE");
        this.adresses         = adresseRepo.findAll();

        // --- Recherche ---
        var searchField = new TextField();
        searchField.setPlaceholder("Rechercher par nom…");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

        // --- Grid ---
        grid = new Grid<>();
        grid.addColumn(BibliothequeResponseDto::getNom)
                .setHeader("Nom")
                .setSortProperty("nom");

        grid.setItemsPageable(pageable ->
                service.findPaginated(searchField.getValue(), pageable).getContent());

        // --- Drawer ---
        drawer = new BibliothequeFormDrawer(
                adresses,
                !isBibliothecaire,
                (id, dto) -> {
                    var saved = service.update(id, dto);
                    grid.getDataProvider().refreshAll();
                    return saved;
                },
                this::handleException
        );

        searchField.addValueChangeListener(e -> grid.getDataProvider().refreshAll());

        grid.addSelectionListener(e ->
                e.getFirstSelectedItem()
                        .map(BibliothequeResponseDto::getId)
                        .ifPresentOrElse(
                                BibliothequeView::showBibliothequeDetail,
                                BibliothequeView::showCatalog
                        )
        );

        // --- Toolbar ---
        var toolbar = new HorizontalLayout(searchField);
        toolbar.setWidthFull();
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.expand(searchField);

        if (isBibliothecaire) {
            var addButton = new Button("Ajouter une bibliothèque", e ->
                    new AddBibliothequeDialog(
                            adresses,
                            dto -> {
                                var saved = service.create(dto);
                                grid.getDataProvider().refreshAll();
                                showBibliothequeDetail(saved.getId());
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
    }

    private void handleException(RuntimeException exception) {
        if (exception instanceof RessourceNotFoundException) {
            showWarning("Ressource introuvable : " + exception.getMessage());
        } else if (exception instanceof AccessDeniedException) {
            showWarning("Action non autorisée : vous devez être bibliothécaire.");
        } else if (exception instanceof org.springframework.dao.DataIntegrityViolationException) {
            showWarning("Violation de contrainte : vérifiez les données saisies.");
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

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer bibliothequeId) {
        if (bibliothequeId != null) {
            grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                    .filter(dto -> dto.getId().equals(bibliothequeId))
                    .findFirst()
                    .ifPresent(grid::select);
        } else {
            grid.deselectAll();
        }

        drawer.setBibliothequeDetail(
                Optional.ofNullable(bibliothequeId)
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

    public static void showBibliothequeDetail(Integer bibliothequeId) {
        UI.getCurrent().navigate(BibliothequeView.class, bibliothequeId);
    }

    public static void showCatalog() {
        UI.getCurrent().navigate(BibliothequeView.class);
    }
}