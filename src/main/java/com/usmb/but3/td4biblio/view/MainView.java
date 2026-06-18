package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.dto.EvenementResponseDto;
import com.usmb.but3.td4biblio.enumeration.ChampRechercheDocument;
import com.usmb.but3.td4biblio.enumeration.TypeDocument;
import com.usmb.but3.td4biblio.enumeration.TypeRecherche;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EvenementService;
import com.usmb.but3.td4biblio.service.SessionService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Page d'accueil de l'application.
 * Affiche les nouvelles acquisitions et les événements à venir sous forme de carrousels,
 * propose une recherche rapide de documents (par titre, type, auteur ou bibliothèque)
 * et donne accès à la connexion / à l'espace utilisateur.
 */
@Route
@PageTitle("Accueil")
@AnonymousAllowed
public final class MainView extends VerticalLayout {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DocumentService documentService;
    private final EvenementService evenementService;
    private final SessionService sessionService;

    /** Critères de recherche proposés sur la page d'accueil. */
    private enum CritereRecherche {
        TITRE("Titre"),
        TYPE_DOCUMENT("Type de document"),
        AUTEUR("Auteur"),
        BIBLIOTHEQUE("Bibliothèque");

        private final String label;

        CritereRecherche(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final ComboBox<CritereRecherche> critereBox = new ComboBox<>("Critère");
    private final ComboBox<TypeRecherche> typeRechercheBox = new ComboBox<>("Type de recherche");
    private final TextField valeurField = new TextField("Valeur recherchée");
    private final ComboBox<TypeDocument> typeDocumentBox = new ComboBox<>("Type de document");
    private final Button searchBtn = new Button("Rechercher", VaadinIcon.SEARCH.create());

    private final Grid<DocumentResponseDto> resultsGrid = new Grid<>(DocumentResponseDto.class, false);
    private final Span noResultsMessage = new Span("Aucun document ne correspond à votre recherche.");

    private final Button accountBtn = new Button();

    public MainView(DocumentService documentService, EvenementService evenementService, SessionService sessionService) {
        this.documentService = documentService;
        this.evenementService = evenementService;
        this.sessionService = sessionService;

        addClassNames(LumoUtility.Padding.MEDIUM);

        add(createHeader());
        add(new HomeCarousel<>(
                "Nouvelles acquisitions",
                documentService.getNewAcquisitions(),
                this::renderDocumentCard,
                "Pas encore de nouvelles acquisitions."
        ));
        add(new HomeCarousel<>(
                "Événements à venir",
                evenementService.getUpcomingEvenements(),
                this::renderEvenementCard,
                "Aucun événement à venir pour le moment."
        ));
        add(createSearchSection());
    }

    // ------------------------------------------------------------------
    // En-tête
    // ------------------------------------------------------------------

    private Component createHeader() {
        H1 title = new H1("Bienvenue à la BiBlio");
        title.addClassNames(LumoUtility.Margin.NONE);

        configureAccountButton();

        HorizontalLayout header = new HorizontalLayout(title, accountBtn);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);
        return header;
    }

    /**
     * Si un utilisateur est connecté, le bouton renvoie vers son espace personnel.
     * Sinon, il renvoie vers la page de connexion.
     */
    private void configureAccountButton() {
        accountBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        if (sessionService.isLoggedIn()) {
            accountBtn.setText("Mon espace");
            accountBtn.setIcon(VaadinIcon.USER.create());
            accountBtn.addClickListener(e -> UI.getCurrent().navigate("espace-utilisateur"));
        } else {
            accountBtn.setText("Se connecter");
            accountBtn.setIcon(VaadinIcon.SIGN_IN.create());
            accountBtn.addClickListener(e -> UI.getCurrent().navigate("login"));
        }
    }

    // ------------------------------------------------------------------
    // Recherche de documents
    // ------------------------------------------------------------------

    private Component createSearchSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.LARGE
        );

        Span sectionTitle = new Span("Rechercher un document");
        sectionTitle.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        critereBox.setItems(CritereRecherche.values());
        critereBox.setItemLabelGenerator(CritereRecherche::getLabel);
        critereBox.setValue(CritereRecherche.TITRE);
        critereBox.addValueChangeListener(e -> updateSearchFieldsVisibility());

        typeRechercheBox.setItems(TypeRecherche.values());
        typeRechercheBox.setItemLabelGenerator(TypeRecherche::getLabel);
        typeRechercheBox.setValue(TypeRecherche.CONTIENT);

        typeDocumentBox.setItems(TypeDocument.values());
        typeDocumentBox.setItemLabelGenerator(TypeDocument::getLabel);

        valeurField.setPlaceholder("Saisir un texte…");
        valeurField.setClearButtonVisible(true);

        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> performSearch());

        updateSearchFieldsVisibility();

        HorizontalLayout fields = new HorizontalLayout(
                critereBox, typeRechercheBox, valeurField, typeDocumentBox, searchBtn
        );
        fields.setAlignItems(FlexComponent.Alignment.END);
        fields.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Margin.Top.SMALL);

        configureResultsGrid();
        noResultsMessage.setVisible(false);
        resultsGrid.setVisible(false);

        section.add(sectionTitle, fields, noResultsMessage, resultsGrid);
        return section;
    }

    /** Le champ "valeur" et le type de recherche n'ont de sens que pour une recherche textuelle. */
    private void updateSearchFieldsVisibility() {
        boolean isType = critereBox.getValue() == CritereRecherche.TYPE_DOCUMENT;
        valeurField.setVisible(!isType);
        typeRechercheBox.setVisible(!isType);
        typeDocumentBox.setVisible(isType);
    }

    private void configureResultsGrid() {
        resultsGrid.addColumn(DocumentResponseDto::getTitre).setHeader("Titre").setAutoWidth(true);
        resultsGrid.addColumn(DocumentResponseDto::getNomAuteur).setHeader("Auteur").setAutoWidth(true);
        resultsGrid.addColumn(DocumentResponseDto::getNomEditeur).setHeader("Éditeur").setAutoWidth(true);
        resultsGrid.addColumn(DocumentResponseDto::getNomBibliotheque).setHeader("Bibliothèque").setAutoWidth(true);
        resultsGrid.addColumn(doc -> doc.getDatePublication() != null ? doc.getDatePublication().format(DATE_FMT) : "")
                .setHeader("Publication").setAutoWidth(true);
        resultsGrid.setHeight("300px");
    }

    private void performSearch() {
        CritereRecherche critere = critereBox.getValue();
        List<DocumentResponseDto> results;

        if (critere == CritereRecherche.TYPE_DOCUMENT) {
            TypeDocument type = typeDocumentBox.getValue();
            if (type == null) {
                Notification.show("Veuillez sélectionner un type de document")
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);
                return;
            }
            results = documentService.getAll(type);
        } else {
            if (!StringUtils.hasText(valeurField.getValue())) {
                Notification.show("Veuillez saisir une valeur à rechercher")
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);
                return;
            }

            ChampRechercheDocument champ = switch (critere) {
                case AUTEUR -> ChampRechercheDocument.AUTEUR;
                case BIBLIOTHEQUE -> ChampRechercheDocument.BIBLIOTHEQUE;
                default -> ChampRechercheDocument.TITRE;
            };

            TypeRecherche type = typeRechercheBox.getValue() == null
                    ? TypeRecherche.CONTIENT
                    : typeRechercheBox.getValue();

            results = documentService.searchDocuments(champ, type, valeurField.getValue());
        }

        resultsGrid.setItems(results);
        resultsGrid.setVisible(!results.isEmpty());
        noResultsMessage.setVisible(results.isEmpty());
    }

    // ------------------------------------------------------------------
    // Cartes des carrousels
    // ------------------------------------------------------------------

    private Component renderDocumentCard(DocumentResponseDto doc) {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Gap.XSMALL
        );
        card.getStyle().set("display", "flex").set("flex-direction", "column").set("cursor", "pointer");
        card.setWidth("220px");
        card.setHeight("270px");
        card.getElement().addEventListener("click", e -> UI.getCurrent().navigate("documents"));

        if (StringUtils.hasText(doc.getGif())) {
            Image img = new Image(doc.getGif(), doc.getTitre());
            img.setWidth("100%");
            img.setHeight("120px");
            img.getStyle().set("object-fit", "cover").set("border-radius", "var(--lumo-border-radius-m)");
            card.add(img);
        } else {
            card.add(createPlaceholder(VaadinIcon.BOOK));
        }

        Span titre = new Span(doc.getTitre());
        titre.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        titre.getStyle()
                .set("display", "-webkit-box")
                .set("-webkit-line-clamp", "2")
                .set("-webkit-box-orient", "vertical")
                .set("overflow", "hidden");

        Span auteur = new Span(StringUtils.hasText(doc.getNomAuteur()) ? doc.getNomAuteur() : "Auteur inconnu");
        auteur.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        Span date = new Span(doc.getDateAcquisition() != null
                ? "Acquis le " + doc.getDateAcquisition().format(DATE_FMT)
                : "");
        date.addClassNames(LumoUtility.TextColor.TERTIARY, LumoUtility.FontSize.XSMALL);

        card.add(titre, auteur, date);
        return card;
    }

    private Component renderEvenementCard(EvenementResponseDto ev) {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Gap.XSMALL
        );
        card.getStyle().set("display", "flex").set("flex-direction", "column").set("cursor", "pointer");
        card.setWidth("220px");
        card.setHeight("180px");
        card.getElement().addEventListener("click",
                e -> UI.getCurrent().navigate("evenements/" + ev.getId()));

        Icon icon = VaadinIcon.CALENDAR.create();
        icon.addClassNames(LumoUtility.TextColor.PRIMARY);
        Span nom = new Span(ev.getNom());
        nom.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        HorizontalLayout header = new HorizontalLayout(icon, nom);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassNames(LumoUtility.Gap.SMALL);

        StringBuilder periode = new StringBuilder();
        if (ev.getDateDebut() != null) {
            periode.append(ev.getDateDebut().format(DATE_FMT));
            if (ev.getDateFin() != null) {
                periode.append(" → ").append(ev.getDateFin().format(DATE_FMT));
            }
        }
        Span dates = new Span(periode.toString());
        dates.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        Span biblio = new Span(StringUtils.hasText(ev.getNomBibliotheque()) ? ev.getNomBibliotheque() : "");
        biblio.addClassNames(LumoUtility.TextColor.TERTIARY, LumoUtility.FontSize.XSMALL);

        card.add(header, dates, biblio);
        return card;
    }

    private Div createPlaceholder(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.setSize("48px");
        icon.addClassNames(LumoUtility.TextColor.SECONDARY);

        Div placeholder = new Div(icon);
        placeholder.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("background", "var(--lumo-contrast-10pct)")
                .set("border-radius", "var(--lumo-border-radius-m)");
        placeholder.setWidth("100%");
        placeholder.setHeight("120px");
        return placeholder;
    }

    /**
     * Conserve le point d'entrée utilisé ailleurs dans l'application pour naviguer vers l'accueil.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}