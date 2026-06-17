package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.UtilisateurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.RegisterRequest;
import com.usmb.but3.td4biblio.dto.UtilisateurResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.UtilisateurMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * La couche Service où se trouve toute la logique métier des utilisateurs.
 * Elle interagit avec la couche Repository pour accéder aux données.
 * Gère l'enregistrement, la mise à jour et l'authentification des utilisateurs.
 * Assure le chiffrage sécurisé des mots de passe et la gestion des adresses.
 */
@Service
@Transactional
public class UtilisateurService extends AbstractGenericService<Utilisateur, Integer, UtilisateurResponseDto, UtilisateurDetailResponseDto, RegisterRequest>{

    private final UserRepository userRepository;
    private final AdresseRepo adresseRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur du service Utilisateur.
     * Injecte les dépendances nécessaires via le constructeur du service abstrait.
     * @param repository - repository des utilisateurs
     * @param mapper - mapper pour la conversion entre entités et DTOs
     * @param adresseRepository - repository des adresses
     * @param passwordEncoder - encodeur de mots de passe pour la sécurité
     */
    public UtilisateurService(UserRepository repository, UtilisateurMapper mapper,
                              AdresseRepo adresseRepository,
                              PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.userRepository = repository;
        this.adresseRepository = adresseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Met à jour un utilisateur existant à partir des données du DTO.
     * @param id - identifiant de l'utilisateur à mettre à jour
     * @param dto - données de mise à jour
     * @return le DTO détaillé de l'utilisateur mis à jour
     * @throws RessourceNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UtilisateurDetailResponseDto update(Integer id, RegisterRequest dto) {
        Utilisateur utilisateur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé : " + id));
        mapper.updateFromDto(dto, utilisateur);
        return mapper.toDetailResponse(repository.save(utilisateur));
    }

    /**
     * Enregistre un nouvel utilisateur dans le système.
     * Crée une nouvelle adresse, chiffre le mot de passe en utilisant la date de naissance,
     * et initialise un abonnement d'une année.
     * @param request - données d'enregistrement de l'utilisateur
     * @throws ResponseStatusException si l'adresse email est déjà utilisée (code HTTP 409)
     */
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        Adresse adresse = new Adresse();
        adresse.setRue(request.getRue());
        adresse.setCodePostal(request.getCodePostal());
        adresse.setVille(request.getVille());
        adresse = adresseRepository.save(adresse);

        String motDePasseDate = request.getDateNaissance()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Utilisateur user = new Utilisateur();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setDateNaissance(request.getDateNaissance());
        user.setDateFinAbonnement(LocalDate.now().plusYears(1));
        user.setNumeroCarte(request.getNumeroCarte());
        user.setHashMotDePasse(passwordEncoder.encode(motDePasseDate));
        user.setRoleUtilisateur(request.getRoleUtilisateur());
        user.setAdresse(adresse);

        userRepository.save(user);
    }
}