/*package com.usmb.but3.td4biblio.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.usmb.but3.td4biblio.entity.Auteur;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;

@SpringBootTest
/**
 *  Test class for the AuteurView component.
 *  Voir : https://vaadin.com/docs/v23/tutorial/unit-and-integration-testing

public class AuteurViewTest {

    @Autowired
    private AuteurView auteurView;

    Logger logger = Logger.getLogger(AuteurViewTest.class.getName());

    @Test
    void testAuteurView() {
        // This test will check if the AuteurView bean is loaded correctly
        // and if it can be instantiated without any issues.
        assert auteurView != null;
    }

    @Test
    public void editorAfficheQuandAuteurSelectionne() {
        Grid<Auteur> grid = auteurView.grid;
        Auteur firstAuteur = getFirstItem(grid);
        logger.info("First Auteur: " + firstAuteur);
        AuteurEditor editor = auteurView.editor;

        assertFalse(editor.isVisible());
        grid.asSingleSelect().setValue(firstAuteur);
        assertTrue(editor.isVisible());
        logger.info(" auteur in editor : " + editor.prenom.getValue() + " " + editor.nom.getValue());
        assertEquals(firstAuteur.getNom(), editor.nom.getValue());
    }

    private Auteur getFirstItem(Grid<Auteur> grid) {
        return((ListDataProvider<Auteur>) grid.getDataProvider()).getItems().iterator().next();
    }

    private Auteur getLastItem(Grid<Auteur> grid) {
        Collection<Auteur> auteurs = ((ListDataProvider<Auteur>) grid.getDataProvider()).getItems();
        List<Auteur> auteurList = new ArrayList<>(auteurs);
        return auteurList.get(auteurList.size() - 1);
    }

    @Test
    public void editorAfficheQuandNewBurtonClicked() {
        AuteurEditor editor = auteurView.editor;
        assertFalse(editor.isVisible());
        auteurView.getAddNewBtn().click(); // Simulate clicking the "Add New" button

        assertTrue(editor.isVisible());
        logger.info(" auteur in editor : " + editor.prenom.getValue() + " " + editor.nom.getValue());
        assertEquals("", editor.prenom.getValue());
        assertEquals("", editor.nom.getValue());
        assertEquals("", editor.nationalite.getValue());
        assertEquals(null, editor.dateNaissance.getValue());
        assertEquals(null, editor.dateDeces.getValue());

        editor.prenom.setValue("Zola");
        editor.nom.setValue("Emile");
        editor.nationalite.setValue("Française");
        editor.dateNaissance.setValue(java.time.LocalDate.of(1840, 4, 2));
        editor.dateDeces.setValue(java.time.LocalDate.of(1902, 9, 29));

        editor.save.click(); // Simulate clicking the "Save" button

        Auteur newAuteur = getLastItem(auteurView.grid);
        logger.info("Le nouvel Auteur doit être le dernier dans la grid : " + newAuteur);
        assertEquals( "Zola",newAuteur.getPrenom());
        assertEquals("Emile", newAuteur.getNom() );
        assertEquals("Française", newAuteur.getNationalite() );
        assertEquals(java.time.LocalDate.of(1840, 4, 2), newAuteur.getDateNaissance());
        assertEquals(java.time.LocalDate.of(1902, 9, 29), newAuteur.getDateDeces());
    }
}
*/