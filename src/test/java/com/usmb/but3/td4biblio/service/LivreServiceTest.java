package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.usmb.but3.td4biblio.entity.Livre;

@SpringBootTest
public class LivreServiceTest {
    @Autowired
    private LivreService livreService;

    @Test
    void testGetLivreById() {
        // This test will check if the LivreService can retrieve a Livre by its ID
        // You can use a mock or a real instance of LivreService to perform this test
        Livre livre = livreService.getLivreById(1);
        assert livre != null;
        assert livre.getId() == 1; // Assuming the Livre with ID 1 exists
        assert livre.getTitre().equals("Notre Dame de Paris"); // Assuming the title of the
        }    
    }
