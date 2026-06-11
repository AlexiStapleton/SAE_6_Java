package com.usmb.but3.td4biblio.view.Users;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@Route(value = "register")
@PageTitle("Register")
public class RegisterView extends VerticalLayout {

    public RegisterView(RegisterEditor editor) {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        HorizontalLayout loginLink = new HorizontalLayout(
                new Span("Déjà un compte ?"),
                new RouterLink("Se connecter", LoginView.class)
        );
        loginLink.setAlignItems(FlexComponent.Alignment.CENTER);

        add(new H2("Créer un compte"), editor, loginLink);
    }
}