package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.DTO.EditeurResponseDto;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.util.StringUtils;

@Route(value = "editeur")
@PageTitle("Les Éditeurs")
@Menu(title = "Les Éditeurs", order = 2, icon = "vaadin:building")
public class EditeurView extends VerticalLayout {

    private final EditeurService editeurService;

    final Grid<EditeurResponseDto> grid;
    final TextField filter;
    private final Button addNewBtn;

    public EditeurView(EditeurService editeurService, EditeurEditor editor) {
        this.editeurService = editeurService;

        this.grid = new Grid<>(EditeurResponseDto.class, false);
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un éditeur", VaadinIcon.PLUS.create());

        // ---- En-tête ----
        Icon headerIcon = VaadinIcon.BUILDING.create();
        headerIcon.addClassNames(LumoUtility.TextColor.PRIMARY, LumoUtility.IconSize.LARGE);
        H2 title = new H2("Gestion des Éditeurs");
        title.addClassNames(LumoUtility.Margin.NONE);
        HorizontalLayout header = new HorizontalLayout(headerIcon, title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Padding.Bottom.MEDIUM);

        // ---- Barre d'action ----
        filter.setPlaceholder("Filtrer par nom...");
        filter.setPrefixComponent(VaadinIcon.SEARCH.create());
        filter.setClearButtonVisible(true);
        filter.setWidth("300px");

        addNewBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        actions.setAlignItems(FlexComponent.Alignment.END);
        actions.addClassNames(LumoUtility.Padding.Bottom.MEDIUM);

        // ---- Carte contenant la grille ----
        VerticalLayout card = new VerticalLayout(actions, grid);
        card.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.MEDIUM
        );

        addClassNames(LumoUtility.Padding.MEDIUM);
        add(header, card, editor);

        configureGrid();

        // ---- Logique ----
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listEditeurs(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                editor.editEditeur(editeurService.getById(e.getValue().getId()));
            } else {
                editor.editEditeur(null);
            }
        });

        addNewBtn.addClickListener(e -> editor.editEditeur(new EditeurResponseDto()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            grid.asSingleSelect().clear();
            listEditeurs(filter.getValue());
        });

        listEditeurs(null);
    }

    private void configureGrid() {
        grid.setHeight("350px");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addColumn(EditeurResponseDto::getId)
                .setHeader("Id").setWidth("60px").setFlexGrow(0);

        grid.addColumn(EditeurResponseDto::getNom)
                .setHeader("Société").setAutoWidth(true);

        grid.addColumn(e -> e.getAdresse() != null ? e.getAdresse().getVille() : "")
                .setHeader("Ville").setAutoWidth(true);

        grid.addComponentColumn(e -> createLinkBadge(e.getLienSiteWeb(), VaadinIcon.GLOBE, "Site web"))
                .setHeader("Site web");

        grid.addComponentColumn(e -> createLinkBadge(e.getLienWikipedia(), VaadinIcon.INFO_CIRCLE, "Wikipédia"))
                .setHeader("Wikipédia");
    }

    private Component createLinkBadge(String url, VaadinIcon vaadinIcon, String label) {
        if (!StringUtils.hasText(url)) {
            Span dash = new Span("—");
            dash.addClassNames(LumoUtility.TextColor.TERTIARY);
            return dash;
        }

        Icon icon = vaadinIcon.create();
        icon.addClassNames(LumoUtility.IconSize.SMALL, LumoUtility.TextColor.PRIMARY);

        Anchor link = new Anchor(url, label);
        link.setTarget("_blank");

        HorizontalLayout layout = new HorizontalLayout(icon, link);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassNames(LumoUtility.Gap.XSMALL);
        return layout;
    }

    void listEditeurs(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(editeurService.getByNomContainingIgnoreCase(filterText));
        } else {
            grid.setItems(editeurService.getAll());
        }
    }
}