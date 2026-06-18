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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Carrousel "diaporama" : un seul élément est visible à la fois.
 * Les flèches précédent/suivant font tourner l'affichage parmi les éléments
 * (5 maximum en général), en boucle.
 *
 * @param <T> le type des éléments affichés dans le carrousel
 */
public class HomeCarousel<T> extends VerticalLayout {

    /** Largeur max partagée avec la barre de recherche, pour rester cohérent visuellement. */
    public static final String MAX_WIDTH = "900px";

    private final Div stage = new Div();
    private final List<Component> cards = new ArrayList<>();
    private final Span counter = new Span();
    private int currentIndex = 0;

    /**
     * @param title         titre affiché au-dessus du carrousel
     * @param items         liste des éléments à afficher (typiquement 5 maximum)
     * @param itemRenderer  fonction transformant un élément en composant "carte"
     * @param emptyMessage  message affiché si la liste est vide
     */
    public HomeCarousel(String title, List<T> items, Function<T, Component> itemRenderer, String emptyMessage) {
        setPadding(false);
        setSpacing(false);
        setWidthFull();
        setMaxWidth(MAX_WIDTH);
        addClassNames(LumoUtility.Margin.Bottom.LARGE);

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        Button prev = new Button(VaadinIcon.CHEVRON_LEFT.create());
        Button next = new Button(VaadinIcon.CHEVRON_RIGHT.create());
        prev.addClickListener(e -> show(currentIndex - 1));
        next.addClickListener(e -> show(currentIndex + 1));

        counter.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        HorizontalLayout navButtons = new HorizontalLayout(prev, counter, next);
        navButtons.setAlignItems(FlexComponent.Alignment.CENTER);
        navButtons.addClassNames(LumoUtility.Gap.XSMALL);

        HorizontalLayout header = new HorizontalLayout(titleSpan, navButtons);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(titleSpan);
        header.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        stage.setWidthFull();
        stage.getStyle()
                .set("display", "flex")
                .set("justify-content", "center");

        if (items == null || items.isEmpty()) {
            Span empty = new Span(emptyMessage);
            empty.addClassNames(LumoUtility.TextColor.SECONDARY);
            stage.add(empty);
            prev.setVisible(false);
            next.setVisible(false);
            counter.setVisible(false);
        } else {
            items.forEach(item -> {
                Component card = itemRenderer.apply(item);
                card.setVisible(false);
                cards.add(card);
                stage.add(card);
            });

            boolean singleItem = cards.size() <= 1;
            prev.setVisible(!singleItem);
            next.setVisible(!singleItem);
            counter.setVisible(!singleItem);

            show(0);
        }

        add(header, stage);
    }

    /** Affiche l'élément à l'index donné (en bouclant si on dépasse les bornes). */
    private void show(int index) {
        if (cards.isEmpty()) {
            return;
        }
        cards.get(currentIndex).setVisible(false);
        currentIndex = ((index % cards.size()) + cards.size()) % cards.size();
        cards.get(currentIndex).setVisible(true);
        counter.setText((currentIndex + 1) + " / " + cards.size());
    }
}