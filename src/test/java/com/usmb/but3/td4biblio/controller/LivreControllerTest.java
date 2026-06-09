package com.usmb.but3.td4biblio.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Livre;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LivreControllerTest {

    @LocalServerPort
	private int port;

 	@Autowired
	private TestRestTemplate restTemplate;

    @BeforeAll
    static void setUp() {
        // This method can be used to set up any required data before all tests run
        // For example, you can initialize the database with some Livre objects
    }

    @AfterAll
    static void tearDown() {
        // This method can be used to clean up any data after all tests have run
        // For example, you can clear the database or reset the state
    }

    @BeforeEach
    void init() {
        // This method can be used to reset the state before each test
        // For example, you can clear the database or set up a specific state
    }   

    @AfterEach
    void cleanUp() {
        // This method can be used to clean up the state after each test
        // For example, you can delete any created Livre objects
        /*         Livre[] livres = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/search?titre=Quatre-vingt treize",
                Livre[].class);

        assertThat(livres).satisfies(l -> {
            assertThat(l).isNotEmpty();
            assertThat(l[0].getTitre()).isEqualTo("Quatre-vingt treize");
            assertThat(l[0].getAuteur().getNom()).isEqualTo("Hugo");});
   
        restTemplate.delete("http://localhost:" + port + "/biblio/livre/" + livres[0].getId());
        // Clean up by deleting the saved Livre,*/

    }

    @Test
    void testGetAllLivres() {
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/",
        //        String.class)).contains("Les Misérables", "Aleph"); // Assuming these are some of the books in the database 	

        // TESTed : New assertion to check the list of Livre objects
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/",
                Livre[].class)).satisfies(livres -> {
                    assertThat(livres).isNotEmpty();
                    assertThat(livres[0].getTitre()).isEqualTo("Notre Dame de Paris");
                    assertThat(livres[1].getTitre()).isEqualTo("Fictions");
                });
    }

    @Test
    void testGetLivreById() {
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
        //        String.class)).contains("Les Misérables");  // Assuming the book with ID 1 is "Les Misérables"	
        
        // TESTed : New assertion to check the Livre object			
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
                Livre.class)).satisfies(livre -> (livre.getTitre()).equals("Notre Dame de Paris"));   
     }

    @Test
    void testGetLivreById_Fail() {
        // TESTed : DO  FAIL		
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
                Livre.class)).satisfies(livre -> (livre.getTitre()).equals("Les Miséreux"));
    }


    @Test
    void testSaveLivre() {

        Auteur vhugo = new Auteur();
        vhugo.setId(1); // Assuming 1 is the ID for Victor Hugo
        vhugo.setNom("Hugo");
        vhugo.setPrenom("Victor");
        vhugo.setNationalite("Française");
        vhugo.setDateNaissance(LocalDate.of(1802, 2, 26));
        vhugo.setDateDeces(LocalDate.of(1885, 5, 22));

        Livre livre = new Livre();
        livre.setTitre("Quatre-vingt treize");
        livre.setNbPages(650);
        livre.setAuteur(vhugo); //
        livre.setDatePublication(LocalDate.of(1842, 4, 3));
        
        this.restTemplate.postForObject("http://localhost:" + port + "/biblio/livre/", livre, String.class);

        /* 
        Livre[] livres = this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/search?titre=Quatre-vingt treize",
                Livre[].class);

        assertThat(livres).satisfies(l -> {
            assertThat(l).isNotEmpty();
            assertThat(l[0].getTitre()).isEqualTo("Quatre-vingt treize");
            assertThat(l[0].getAuteur().getNom()).isEqualTo("Hugo");});
   
        restTemplate.delete("http://localhost:" + port + "/biblio/livre/" + livres[0].getId());
        // Clean up by deleting the saved Livre,
           */  
    }

        @Test
    void testGetLivresByAuteurId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/auteur/1",
                Livre[].class)).satisfies(livres -> {
                    assertThat(livres).isNotEmpty();
                    assertThat(livres[0].getTitre()).isEqualTo("Notre Dame de Paris");
                    assertThat(livres[1].getTitre()).isEqualTo("Han d'Islande");
                });
    }


}
