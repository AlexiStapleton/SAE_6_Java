package com.usmb.but3.td4biblio.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmb.but3.td4biblio.entity.Livre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class LivreControllerMockTest {

	@Test
	void testWithMockMvcTester(@Autowired MockMvcTester mvc) {
     assertThat(mvc.get().uri("http://localhost:8080/biblio/livre/1"))
                        .bodyText().contains("Notre Dame de Paris");  // Assuming the book with ID 1 is "Les Misérables"				
    }

   @Test
    void testGetLivreByIdWithMockMvc(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/biblio/livre/1")
                    .accept(MediaType.APPLICATION_JSON)).andReturn();
        String json = result.getResponse().getContentAsString();
        Livre livre = objectMapper.readValue(json, Livre.class);
        assertThat(livre.getTitre()).isEqualTo("Notre Dame de Paris");  // Assuming the book with ID 1 is "Notre-Dame de Paris"
    }

    // TODO : Implement the other tests using MockMvc as done above
    /*     @Test
    void testGetAllLivres() {
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/",
        //        String.class)).contains("Les Misérables", "Aleph"); // Assuming these are some of the books in the database 				
        // TESTed : New assertion to check the list of Livre objects
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/",
                Livre[].class)).satisfies(livres -> {
                    assertThat(livres).isNotEmpty();
                    assertThat(livres[0].getTitre()).isEqualTo("Les Misérables");
                    assertThat(livres[1].getTitre()).isEqualTo("L'Aleph");
                });
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/",
        //        String.class)).contains("Les Misérables", "Aleph"); // Assuming these are some of the books in the database 				
    }

    @Test
    void testGetLivreById() {
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
        //        String.class)).contains("Les Misérables");  // Assuming the book with ID 1 is "Les Misérables"	
        // TESTed : New assertion to check the Livre object			
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
                Livre.class)).satisfies(livre -> assertThat(livre.getTitre()).isEqualTo("Les Misérables"));  // Assuming the book with ID 1 is "Les Misérables"				
        // TESTed : DO  FAIL : New assertion to check the Livre object			
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
        //        Livre.class)).satisfies(livre -> assertThat(livre.getTitre()).isEqualTo("Les Miséreux"));  // Assuming the book with ID 1 is "Les Misérables"				
    }


    @Test
    void testSaveLivre() {
        Livre livre = new Livre();
        livre.setId(3);
        livre.setTitre("Jacques le fataliste");
        livre.setAuteur("Diderot");
        livre.setDatePublication(LocalDate.of(1762, 4, 3));
        
        this.restTemplate.postForObject("http://localhost:" + port + "/biblio/livre/", 
                                                    livre,
                String.class);

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/3",
                String.class)).contains("Jacques le fataliste");				
        
    }

    @Test
    void testUpdateLivre() {

    }
    */
}
