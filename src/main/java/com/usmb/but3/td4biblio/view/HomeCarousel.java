package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.function.Function;

/**
 * Composant générique de carrousel horizontal défilant.
 * N'utilise aucune dépendance externe : un simple conteneur avec défilement
 * horizontal natif (overflow-x) et deux boutons de navigation (précédent / suivant)
 * qui font défiler le conteneur via JavaScript.
 *
 * @param <T> le type des éléments affichés dans le carrousel
 */
public class HomeCarousel<T> extends VerticalLayout {

    private final Div track = new Div();

    /**
     * @param title         titre affiché au-dessus du carrousel
     * @param items         liste des éléments à afficher (typiquement 5 maximum)
     * @param itemRenderer  fonction transformant un élément en composant "carte"
     * @param emptyMessage  message affiché si la liste est vide
     */
    public HomeCarousel(String title, List<T> items, Function<T, Component> itemRenderer, String emptyMessage) {
        setPadding(false);
        setSpacing(false);
        addClassNames(LumoUtility.Margin.Bottom.LARGE);

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        Button prev = new Button(VaadinIcon.CHEVRON_LEFT.create());
        Button next = new Button(VaadinIcon.CHEVRON_RIGHT.create());
        prev.addClickListener(e -> scrollBy(-260));
        next.addClickListener(e -> scrollBy(260));

        HorizontalLayout navButtons = new HorizontalLayout(prev, next);
        navButtons.addClassNames(LumoUtility.Gap.XSMALL);

        HorizontalLayout header = new HorizontalLayout(titleSpan, navButtons);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(titleSpan);
        header.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        track.getStyle()
                .set("display", "flex")
                .set("gap", "var(--lumo-space-m)")
                .set("overflow-x", "auto")
                .set("scroll-snap-type", "x mandatory")
                .set("scroll-behavior", "smooth")
                .set("padding-bottom", "var(--lumo-space-s)");

        if (items == null || items.isEmpty()) {
            Span empty = new Span(emptyMessage);
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            track.add(empty);
        } else {
            items.forEach(item -> {
                Component card = itemRenderer.apply(item);
                card.getElement().getStyle()
                        .set("scroll-snap-align", "start")
                        .set("flex", "0 0 auto");
                track.add(card);
            });
        }

        add(header, track);
    }

    private void scrollBy(int pixels) {
        track.getElement().executeJs("this.scrollBy({left: $0, behavior: 'smooth'})", pixels);
    }
}