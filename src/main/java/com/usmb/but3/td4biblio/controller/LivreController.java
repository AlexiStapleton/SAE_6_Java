package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.service.LivreService;

import java.util.List;

/**
 * La classe Controller où sont traitées  toutes les requests de l'utilisateur et où les
 * réponses appropriées sont renvoyées.
 * Elle interagit avec la couche Service pour accéder aux données.
 */
@RestController
@RequestMapping("/biblio/livre")
@RequiredArgsConstructor
@Validated
public class LivreController {

 private final LivreService livreService;

 /**
  * This method is called when a GET request is made
  * URL: localhost:8080/livre/
  * Purpose: Fetches all the livres in the livre table
  * @return List of Livres
  */
 @GetMapping("/")
 public ResponseEntity<List<Livre>> getAllLivres(){
     return ResponseEntity.ok().body(livreService.getAllLivres());
 }

 /**
  * This method is called when a GET request is made
  * URL: localhost:8080/livre/1 (or any other id)
  * Purpose: Fetches livre with the given id
  * @param id - livre id
  * @return Livre with the given id
  */
 @GetMapping("/{id}")
 public ResponseEntity<Livre> getLivreById(@PathVariable("id") Integer id)
 {
    //return ResponseEntity.ok().body(livreService.getLivreById(id));

    Livre livre = livreService.getLivreById(id);
    if (livre == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.ok().body(livre);
 }

 /**
  * This method is called when a POST request is made
  * URL: localhost:8080/livre/
  * Purpose: Save an Livre entity
  * @param livre - Request body is an Livre entity
  * @return Saved Livre entity
  */
 @PostMapping("/")
 public ResponseEntity<Livre> saveLivre(@RequestBody Livre livre)
 {
     return ResponseEntity.ok().body(livreService.saveLivre(livre));
 }

 /**
  * This method is called when a PUT request is made
  * URL: localhost:8080/livre/
  * Purpose: Update an Livre entity
  * @param livre - Livre entity to be updated
  * @return Updated Livre
  */
 @PutMapping("/")
 public ResponseEntity<Livre> updateLivre(@RequestBody Livre livre)
 {
     return ResponseEntity.ok().body(livreService.updateLivre(livre));
 }

 /**
  * This method is called when a PUT request is made
  * URL: localhost:8080/livre/1 (or any other id)
  * Purpose: Delete an Livre entity
  * @param id - livre's id to be deleted
  * @return a String message indicating livre record has been deleted successfully
  */
 @DeleteMapping("/{id}")
 public ResponseEntity<String> deleteLivreById(@PathVariable("id") Integer id)
 {
    livreService.deleteLivreById(id);
    return ResponseEntity.ok().body("Deleted livre successfully");
 }

    /**
     * GET livres by auteur id.
     * URL: localhost:8080/biblio/livre/auteur/{auteurId}
     */
    @GetMapping("/auteur/{auteurId}")
    public ResponseEntity<List<Livre>> getLivresByAuteurId(@PathVariable("auteurId") Integer auteurId) {
        return ResponseEntity.ok().body(livreService.getByAuteurId(auteurId));
    }

     /**
     * GET livres by titre (--like--) as Request Parameters.
     * URL: localhost:8080/biblio/livre/search?titre=miséra
     */
    @GetMapping("/search")
    public ResponseEntity<List<Livre>> getLivresByTitreContaining(@RequestParam(name="titre") String titre) {
        return ResponseEntity.ok().body(livreService.getByTitreContainingIgnoreCase(titre));
    }


}
