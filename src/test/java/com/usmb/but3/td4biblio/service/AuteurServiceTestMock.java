package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.AuteurCreateDto;
import com.usmb.but3.td4biblio.dto.AuteurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.AuteurMapper;
import com.usmb.but3.td4biblio.repository.AuteurRepo;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuteurServiceTestMock {

    @Mock
    private AuteurRepo auteurRepo;

    @Mock
    private TypeAuteurRepo typeAuteurRepo;

    @Mock
    private AuteurMapper mapper;

    private AuteurService auteurService;

    // ─── Fixtures ────────────────────────────────────────────────────────────

    private Auteur auteur;
    private AuteurCreateDto createDto;
    private AuteurResponseDto responseDto;
    private AuteurDetailResponseDto detailResponseDto;

    @BeforeEach
    void setUp() {
        auteurService = new AuteurService(auteurRepo, mapper, typeAuteurRepo);

        auteur = new Auteur();
        auteur.setId(1);
        auteur.setNom("Hugo");
        auteur.setPrenom("Victor");
        auteur.setNationalite("Française");
        auteur.setDateNaissance(LocalDate.of(1802, 2, 26));
        auteur.setDateDeces(LocalDate.of(1885, 5, 22));
        auteur.setVilleNaissance("Besançon");
        auteur.setLienWikipedia("https://fr.wikipedia.org/wiki/Victor_Hugo");
        auteur.setTypesAuteur(List.of());
        auteur.setDocuments(List.of());

        createDto = new AuteurCreateDto(
                "Hugo",
                "Victor",
                "Française",
                LocalDate.of(1802, 2, 26),
                LocalDate.of(1885, 5, 22),
                "Besançon",
                "https://fr.wikipedia.org/wiki/Victor_Hugo",
                List.of(1, 2)
        );

        responseDto = new AuteurResponseDto(
                1,
                "Hugo",
                "Victor",
                "Française",
                LocalDate.of(1802, 2, 26),
                LocalDate.of(1885, 5, 22),
                "Besançon",
                "https://fr.wikipedia.org/wiki/Victor_Hugo"
        );

        detailResponseDto = new AuteurDetailResponseDto(
                1,
                "Hugo",
                "Victor",
                "Française",
                LocalDate.of(1802, 2, 26),
                LocalDate.of(1885, 5, 22),
                "Besançon",
                "https://fr.wikipedia.org/wiki/Victor_Hugo",
                List.of(),
                List.of()
        );
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les auteurs")
    void getAll_retourneTousLesAuteurs() {
        when(auteurRepo.findAll()).thenReturn(List.of(auteur));
        when(mapper.toResponse(auteur)).thenReturn(responseDto);

        List<AuteurResponseDto> result = auteurService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Hugo");
        verify(auteurRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun auteur")
    void getAll_retourneListeVide() {
        when(auteurRepo.findAll()).thenReturn(List.of());

        List<AuteurResponseDto> result = auteurService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'un auteur existant")
    void getById_retourneAuteurExistant() {
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(mapper.toDetailResponse(auteur)).thenReturn(detailResponseDto);

        AuteurDetailResponseDto result = auteurService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Hugo");
    }

    @Test
    @DisplayName("getById - lève une exception si l'auteur n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(auteurRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auteurService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne le nouvel auteur")
    void create_retourneNouvelAuteur() {
        when(mapper.toEntity(createDto)).thenReturn(auteur);
        when(auteurRepo.save(auteur)).thenReturn(auteur);
        when(mapper.toDetailResponse(auteur)).thenReturn(detailResponseDto);

        AuteurDetailResponseDto result = auteurService.create(createDto);

        assertThat(result.getNom()).isEqualTo("Hugo");
        verify(auteurRepo).save(auteur);
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne l'auteur modifié")
    void update_retourneAuteurMisAJour() {
        TypeAuteur type1 = new TypeAuteur();
        type1.setId(1);
        TypeAuteur type2 = new TypeAuteur();
        type2.setId(2);

        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(typeAuteurRepo.findAllById(List.of(1, 2))).thenReturn(List.of(type1, type2));
        when(auteurRepo.save(auteur)).thenReturn(auteur);
        when(mapper.toDetailResponse(auteur)).thenReturn(detailResponseDto);

        AuteurDetailResponseDto result = auteurService.update(1, createDto);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(createDto, auteur);
        verify(auteurRepo).save(auteur);
    }

    @Test
    @DisplayName("update - lève une exception si l'auteur n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(auteurRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auteurService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(auteurRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime l'auteur existant")
    void delete_supprime() {
        when(auteurRepo.existsById(1)).thenReturn(true);

        auteurService.delete(1);

        verify(auteurRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si l'auteur n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(auteurRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> auteurService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(auteurRepo, never()).deleteById(any());
    }

    // ─── getAuteursByNom ──────────────────────────────────────────────────────

    @Test
    @DisplayName("getAuteursByNom - retourne les auteurs avec le nom exact")
    void getAuteursByNom_retourneAuteurs() {
        when(auteurRepo.findByNom("Hugo")).thenReturn(List.of(auteur));
        when(mapper.toResponse(auteur)).thenReturn(responseDto);

        List<AuteurResponseDto> result = auteurService.getAuteursByNom("Hugo");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Hugo");
    }

    @Test
    @DisplayName("getAuteursByNom - retourne une liste vide si aucun résultat")
    void getAuteursByNom_retourneListeVide() {
        when(auteurRepo.findByNom("Inconnu")).thenReturn(List.of());

        List<AuteurResponseDto> result = auteurService.getAuteursByNom("Inconnu");

        assertThat(result).isEmpty();
    }

    // ─── getAuteursByNomAndPrenom ─────────────────────────────────────────────

    @Test
    @DisplayName("getAuteursByNomAndPrenom - retourne les auteurs avec le nom et prénom exacts")
    void getAuteursByNomAndPrenom_retourneAuteurs() {
        when(auteurRepo.findByNomAndPrenom("Hugo", "Victor")).thenReturn(List.of(auteur));
        when(mapper.toResponse(auteur)).thenReturn(responseDto);

        List<AuteurResponseDto> result = auteurService.getAuteursByNomAndPrenom("Hugo", "Victor");

        assertThat(result).hasSize(1);
    }

    // ─── getByNomContainingIgnoreCase ─────────────────────────────────────────

    @Test
    @DisplayName("getByNomContainingIgnoreCase - retourne les auteurs dont le nom contient le filtre")
    void getByNomContainingIgnoreCase_retourneAuteurs() {
        when(auteurRepo.findByNomContainingIgnoreCase("hug")).thenReturn(List.of(auteur));
        when(mapper.toResponse(auteur)).thenReturn(responseDto);

        List<AuteurResponseDto> result = auteurService.getByNomContainingIgnoreCase("hug");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Hugo");
    }

    @Test
    @DisplayName("getByNomContainingIgnoreCase - insensible à la casse")
    void getByNomContainingIgnoreCase_insensibleCasse() {
        when(auteurRepo.findByNomContainingIgnoreCase("HUG")).thenReturn(List.of(auteur));
        when(mapper.toResponse(auteur)).thenReturn(responseDto);

        List<AuteurResponseDto> result = auteurService.getByNomContainingIgnoreCase("HUG");

        assertThat(result).hasSize(1);
    }

    // ─── getAuteursByNomStartWithIgnoreCase ───────────────────────────────────

    @Test
    @DisplayName("getAuteursByNomStartWithIgnoreCase - retourne les auteurs dont le nom commence par le filtre")
    void getAuteursByNomStartWithIgnoreCase_retourneAuteurs() {
        when(auteurRepo.findByNomStartsWithIgnoreCase("Hu")).thenReturn(List.of(auteur));
        when(mapper.toResponse(auteur)).thenReturn(responseDto);

        List<AuteurResponseDto> result = auteurService.getAuteursByNomStartWithIgnoreCase("Hu");

        assertThat(result).hasSize(1);
    }
}