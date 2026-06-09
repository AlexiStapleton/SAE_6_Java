package com.usmb.but3.td4biblio.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.repository.AuteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuteurService {

    private final AuteurRepo auteurRepo;

    public List<Auteur> getAllAuteurs() {
        //return auteurRepo.findAll();
        // To specify a sort order, use:
        return auteurRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Auteur getAuteurById(Integer id) {
        return auteurRepo.findById(id).orElse(null);
    }

    public Auteur saveAuteur(Auteur auteur) {
        return auteurRepo.save(auteur);
    }

    public Auteur updateAuteur(Auteur auteur) {
        return auteurRepo.save(auteur);
    }

    public void deleteAuteurById(Integer id) {
        auteurRepo.deleteById(id);
    }
    public List<Auteur> getAuteursByNom(String nom) {
        return auteurRepo.findByNom(nom);
    }
    public List<Auteur> getAuteursByNomAndPrenom(String nom, String prenom) {
        return auteurRepo.findByNomAndPrenom(nom, prenom);
    }
    public List<Auteur> getAuteursByNomLikeAndPrenomLike(String nom, String prenom) {
        //return auteurRepo.findByNomLikeAndPrenomLike("%" + nom + "%", "%" + prenom + "%");
        return auteurRepo.findByNomLikeAndPrenomLike(nom,prenom);
    }  

    public List<Auteur> getAuteursByNomStartWithIgnoreCase(String filter) {
        return auteurRepo.findByNomStartsWithIgnoreCase(filter);
    }  

    public List<Auteur> getByNomContainingIgnoreCase(String filter) {
       return auteurRepo.findByNomContainingIgnoreCase(filter);
    }

}
