package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.service.SessionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

import java.util.Set;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Layout
public final class MainLayout extends AppLayout {

    private static final Set<String> NO_MENU_ROUTES = Set.of("login", "register");

    private final SessionService sessionService;
    private final VerticalLayout drawerContent;
    private final Span userLabel = new Span();

    public MainLayout(SessionService sessionService) {
        this.sessionService = sessionService;

        setPrimarySection(Section.DRAWER);

        drawerContent = new VerticalLayout();
        drawerContent.setSizeFull();
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);

        drawerContent.add(createHeader());

        Scroller scroller = new Scroller(createSideNav());
        scroller.setSizeFull();
        drawerContent.add(scroller);
        drawerContent.expand(scroller);

        drawerContent.add(createLogoutButton());

        addToDrawer(drawerContent);

        UI.getCurrent().addAfterNavigationListener(this::onAfterNavigation);
    }

    private void onAfterNavigation(AfterNavigationEvent event) {
        String path = event.getLocation().getFirstSegment();
        boolean hideMenu = NO_MENU_ROUTES.contains(path);
        setDrawerOpened(!hideMenu);
        getElement().getThemeList().set("hide-drawer", hideMenu);
        userLabel.setText(
                sessionService.getCurrentUser()
                        .map(u -> u.getPrenom() + " " + u.getNom())
                        .orElse("BiBlio Vaadin")
        );
    }

    private Div createHeader() {
        var appLogo = VaadinIcon.CUBES.create();
        appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

        userLabel.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        var header = new Div(appLogo, userLabel);
        header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.MEDIUM);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }

    private Div createLogoutButton() {
        Button logoutBtn = new Button("Se déconnecter", VaadinIcon.SIGN_OUT.create());
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        logoutBtn.addClickListener(e -> {
            sessionService.logout();
            UI.getCurrent().getPage().setLocation("/login");
        });

        Div wrapper = new Div(logoutBtn);
        wrapper.addClassNames(Padding.MEDIUM, Display.FLEX, JustifyContent.CENTER);
        return wrapper;
    }
}