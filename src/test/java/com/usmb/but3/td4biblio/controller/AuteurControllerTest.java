// package com.usmb.but3.td4biblio.controller;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import com.usmb.but3.td4biblio.entity.Auteur;
// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class AuteurControllerTest {

//     @LocalServerPort
// 	private int port;

//  	@Autowired
// 	private TestRestTemplate restTemplate;

//     @BeforeAll
//     static void setUp() {
//         // This method can be used to set up any required data before all tests run
//         // For example, you can initialize the database with some Livre objects
//     }

//     @BeforeEach
//     void init() {
//         // This method can be used to reset the state before each test
//         // For example, you can clear the database or set up a specific state
//     }   

//     @Test
//     void testGetAllAuteurs() {

//         assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/",
//                 Auteur[].class)).satisfies(auteurs -> {
//                     assertThat(auteurs).isNotEmpty();
//                     assertThat(auteurs[0].getNom()).isEqualTo("Hugo");
//                     assertThat(auteurs[1].getNom()).isEqualTo("Borges");
//                 });
//     }

//     @Test
//     void testGetAuteurById() {
//         //OK :
//         //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/1",
//         //        Auteur.class)).satisfies(livre -> assertThat(livre.getNom()).equals("Hugo"));  // Assuming the book with ID 1 is "Les Misérables"				

//         // OK : much simpler :
//         assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/1",
//                 Auteur.class)).satisfies(auteur -> (auteur.getId()).equals(1));  				

 
//         // TESTed : DO  FAIL : New assertion to check the Livre object			
//         //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
//         //        Livre.class)).satisfies(livre -> assertThat(livre.getTitre()).isEqualTo("Les Miséreux"));  // Assuming the book with ID 1 is "Les Misérables"				
//     }

//     @Test
//     void testGetAuteursByNomLikeAndPrenomLike() {

//         assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/searchLike?nom=Hugo&prenom=Victor",
//                 Auteur[].class)).satisfies(auteurs -> {
//                     assertThat(auteurs).isNotEmpty();
//                     assertThat(auteurs[0].getNom()).isEqualTo("Hugo");
//                 });
//     }

//     /**
//      * GET auteurs by nom.
//      * URL: localhost:8080/biblio/auteur/nom/{nom}
//      */
//     @Test //("/nom/{nom}")
//     void  testGetAuteursByNom() {
//         assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/nom/Hugo",
//                 Auteur[].class)).satisfies(auteurs -> {
//                     assertThat(auteurs).isNotEmpty();
//                     assertThat(auteurs[0].getNom()).isEqualTo("Hugo");
//                 });
//     }

// }