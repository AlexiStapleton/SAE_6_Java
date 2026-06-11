//package com.usmb.but3.td4biblio.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import com.usmb.but3.td4biblio.entity.Auteur;
//import com.usmb.but3.td4biblio.service.AuteurService;
//
//import java.util.List;
//
///**
// * Controller for handling Auteur-related HTTP requests.
// */
//@RestController
//@RequestMapping("/biblio/auteur")
//@RequiredArgsConstructor
//@Validated
//public class AuteurController {
//
//    private final AuteurService auteurService;
//
//    /**
//     * GET all auteurs.
//     * URL: localhost:8080/biblio/auteur/
//     */
//    @GetMapping("/")
//    public ResponseEntity<List<Auteur>> getAllAuteurs() {
//        return ResponseEntity.ok().body(auteurService.getAllAuteurs());
//    }
//
//    /**
//     * GET auteur by id.
//     * URL: localhost:8080/biblio/auteur/{id}
//     */
//    @GetMapping("/{id}")
//
//    //public ResponseEntity<Auteur> getAuteurById(@PathVariable() Integer id) { // Corrected to use @PathVariable("id") for clarity
//    public ResponseEntity<Auteur> getAuteurById(@PathVariable("id") Integer id) {
//        return ResponseEntity.ok().body(auteurService.getAuteurById(id));
//    }
//
//    /**
//     * GET auteurs by nom.
//     * URL: localhost:8080/biblio/auteur/nom/{nom}
//     */
//    @GetMapping("/nom/{nom}")
//    public ResponseEntity<List<Auteur>> getAuteursByNom(@PathVariable("nom") String nom) {
//        return ResponseEntity.ok().body(auteurService.getAuteursByNom(nom));
//    }
//
//    /**
//     * GET auteurs by nom and prenom. as Request Parameters.
//     * URL: localhost:8080/biblio/auteur/search?nom={nom}&prenom={prenom}
//     */
//    @GetMapping("/search")
//    public ResponseEntity<List<Auteur>> getAuteursByNomAndPrenom(@RequestParam(name="nom") String nom, @RequestParam(name="prenom") String prenom) {
//        return ResponseEntity.ok().body(auteurService.getAuteursByNomAndPrenom(nom, prenom));
//    }
//
//    /**
//     * GET auteurs by nom and prenom (--like--) as Request Parameters.
//     * URL: localhost:8080/biblio/auteur/searchLike?nom=Hug&prenom=Vict
//     */
//    @GetMapping("/searchLike")
//    public ResponseEntity<List<Auteur>> getAuteursByNomLikeAndPrenomLike(@RequestParam(name="nom") String nom, @RequestParam(name="prenom") String prenom) {
//        return ResponseEntity.ok().body(auteurService.getAuteursByNomLikeAndPrenomLike(nom, prenom));
//    }
//
//    /**
//     * POST a new auteur.
//     * URL: localhost:8080/biblio/auteur/
//     */
//    @PostMapping("/")
//    public ResponseEntity<Auteur> saveAuteur(@RequestBody Auteur auteur) {
//        return ResponseEntity.ok().body(auteurService.saveAuteur(auteur));
//    }
//
//    /**
//     * PUT (update) an auteur.
//     * URL: localhost:8080/biblio/auteur/
//     */
//    @PutMapping("/")
//    public ResponseEntity<Auteur> updateAuteur(@RequestBody Auteur auteur) {
//        return ResponseEntity.ok().body(auteurService.updateAuteur(auteur));
//    }
//
//    /**
//     * DELETE an auteur by id.
//     * URL: localhost:8080/biblio/auteur/{id}
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteAuteurById(@PathVariable Integer id) {
//        auteurService.deleteAuteurById(id);
//        return ResponseEntity.ok().body("Deleted auteur successfully");
//    }
//}