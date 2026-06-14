package com.usmb.but3.td4biblio.service.mock;

import com.usmb.but3.td4biblio.dto.RegisterRequest;
import com.usmb.but3.td4biblio.dto.UtilisateurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.UtilisateurResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.UtilisateurMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.UserRepository;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private AdresseRepo adresseRepo;
    @Mock private UtilisateurMapper mapper;
    @Mock private PasswordEncoder passwordEncoder;

    private UtilisateurService utilisateurService;

    private Utilisateur utilisateur;
    private RegisterRequest registerRequest;
    private UtilisateurResponseDto responseDto;
    private UtilisateurDetailResponseDto detailResponseDto;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        utilisateurService = new UtilisateurService(userRepository, mapper, adresseRepo, passwordEncoder);

        adresse = new Adresse();
        adresse.setId(1);
        adresse.setRue("1 rue de la Paix");
        adresse.setCodePostal("75001");
        adresse.setVille("Paris");

        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setNom("Dupont");
        utilisateur.setPrenom("Jean");
        utilisateur.setEmail("jean.dupont@email.com");
        utilisateur.setDateNaissance(LocalDate.of(1990, 1, 1));
        utilisateur.setAdresse(adresse);
        utilisateur.setRoleUtilisateur(RoleUtilisateur.EMPRUNTEUR);

        registerRequest = new RegisterRequest();
        registerRequest.setNom("Dupont");
        registerRequest.setPrenom("Jean");
        registerRequest.setEmail("jean.dupont@email.com");
        registerRequest.setDateNaissance(LocalDate.of(1990, 1, 1));
        registerRequest.setRoleUtilisateur(RoleUtilisateur.EMPRUNTEUR);
        registerRequest.setRue("1 rue de la Paix");
        registerRequest.setCodePostal("75001");
        registerRequest.setVille("Paris");

        responseDto = new UtilisateurResponseDto();
        responseDto.setId(1);
        responseDto.setNom("Dupont");

        detailResponseDto = new UtilisateurDetailResponseDto();
        detailResponseDto.setId(1);
        detailResponseDto.setNom("Dupont");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les utilisateurs")
    void getAll_retourneTousLesUtilisateurs() {
        when(userRepository.findAll()).thenReturn(List.of(utilisateur));
        when(mapper.toResponse(utilisateur)).thenReturn(responseDto);

        List<UtilisateurResponseDto> result = utilisateurService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun utilisateur")
    void getAll_retourneListeVide() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UtilisateurResponseDto> result = utilisateurService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'un utilisateur existant")
    void getById_retourneUtilisateurExistant() {
        when(userRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(mapper.toDetailResponse(utilisateur)).thenReturn(detailResponseDto);

        UtilisateurDetailResponseDto result = utilisateurService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Dupont");
    }

    @Test
    @DisplayName("getById - lève une exception si l'utilisateur n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilisateurService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne l'utilisateur modifié")
    void update_retourneUtilisateurMisAJour() {
        when(userRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(userRepository.save(utilisateur)).thenReturn(utilisateur);
        when(mapper.toDetailResponse(utilisateur)).thenReturn(detailResponseDto);

        UtilisateurDetailResponseDto result = utilisateurService.update(1, registerRequest);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(registerRequest, utilisateur);
        verify(userRepository).save(utilisateur);
    }

    @Test
    @DisplayName("update - lève une exception si l'utilisateur n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilisateurService.update(99, registerRequest))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(userRepository, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime l'utilisateur existant")
    void delete_supprime() {
        when(userRepository.existsById(1)).thenReturn(true);

        utilisateurService.delete(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si l'utilisateur n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> utilisateurService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(userRepository, never()).deleteById(any());
    }

    // ─── register ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("register - crée un nouvel utilisateur avec adresse et mot de passe hashé")
    void register_creeNouvelUtilisateur() {
        when(userRepository.existsByEmail("jean.dupont@email.com")).thenReturn(false);
        when(adresseRepo.save(any(Adresse.class))).thenReturn(adresse);
        when(passwordEncoder.encode(any())).thenReturn("hashed_password");

        utilisateurService.register(registerRequest);

        verify(adresseRepo).save(any(Adresse.class));
        verify(passwordEncoder).encode("01/01/1990");
        verify(userRepository).save(argThat(u ->
                u.getNom().equals("Dupont") &&
                        u.getEmail().equals("jean.dupont@email.com") &&
                        u.getHashMotDePasse().equals("hashed_password") &&
                        u.getAdresse().equals(adresse) &&
                        u.getDateFinAbonnement().equals(LocalDate.now().plusYears(1))
        ));
    }

    @Test
    @DisplayName("register - lève une exception si l'email est déjà utilisé")
    void register_leveExceptionSiEmailExistant() {
        when(userRepository.existsByEmail("jean.dupont@email.com")).thenReturn(true);

        assertThatThrownBy(() -> utilisateurService.register(registerRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email déjà utilisé");

        verify(userRepository, never()).save(any());
        verify(adresseRepo, never()).save(any());
    }

    @Test
    @DisplayName("register - le mot de passe est hashé à partir de la date de naissance")
    void register_motDePasseHasheDepuisDateNaissance() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(adresseRepo.save(any())).thenReturn(adresse);
        when(passwordEncoder.encode("01/01/1990")).thenReturn("hashed");

        utilisateurService.register(registerRequest);

        verify(passwordEncoder).encode("01/01/1990");
    }

    @Test
    @DisplayName("create - crée et retourne le nouvel utilisateur")
    void create_retourneNouvelUtilisateur() {
        when(mapper.toEntity(registerRequest)).thenReturn(utilisateur);
        when(userRepository.save(utilisateur)).thenReturn(utilisateur);
        when(mapper.toDetailResponse(utilisateur)).thenReturn(detailResponseDto);

        UtilisateurDetailResponseDto result = utilisateurService.create(registerRequest);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dupont");
        verify(userRepository).save(utilisateur);
    }
}