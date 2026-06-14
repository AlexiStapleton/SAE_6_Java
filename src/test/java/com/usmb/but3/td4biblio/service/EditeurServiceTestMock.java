package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EditeurCreateDto;
import com.usmb.but3.td4biblio.dto.EditeurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EditeurMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditeurServiceTestMock {

    @Mock
    private EditeurRepo editeurRepo;

    @Mock
    private AdresseRepo adresseRepo;

    @Mock
    private EditeurMapper mapper;

    private EditeurService editeurService;

    // ─── Fixtures ────────────────────────────────────────────────────────────

    private Editeur editeur;
    private EditeurCreateDto createDto;
    private EditeurResponseDto responseDto;
    private EditeurDetailResponseDto detailResponseDto;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        editeurService = new EditeurService(editeurRepo, mapper, adresseRepo);

        adresse = new Adresse();
        adresse.setId(1);

        editeur = new Editeur();
        editeur.setId(1);
        editeur.setNom("Gallimard");
        editeur.setLienSiteWeb("https://www.gallimard.fr");
        editeur.setLienWikipedia("https://fr.wikipedia.org/wiki/Gallimard");
        editeur.setAdresse(adresse);
        editeur.setDocuments(List.of());

        createDto = new EditeurCreateDto(
                "Gallimard",
                "https://www.gallimard.fr",
                "https://fr.wikipedia.org/wiki/Gallimard",
                1
        );

        responseDto = new EditeurResponseDto(
                1,
                "Gallimard",
                "https://www.gallimard.fr",
                "https://fr.wikipedia.org/wiki/Gallimard",
                null  // ← adresse
        );

        detailResponseDto = new EditeurDetailResponseDto(
                1,
                "Gallimard",
                "https://www.gallimard.fr",
                "https://fr.wikipedia.org/wiki/Gallimard",
                null,  // ← adresse
                List.of()
        );
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les éditeurs")
    void getAll_retourneTousLesEditeurs() {
        when(editeurRepo.findAll()).thenReturn(List.of(editeur));
        when(mapper.toResponse(editeur)).thenReturn(responseDto);

        List<EditeurResponseDto> result = editeurService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Gallimard");
        verify(editeurRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun éditeur")
    void getAll_retourneListeVide() {
        when(editeurRepo.findAll()).thenReturn(List.of());

        List<EditeurResponseDto> result = editeurService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'un éditeur existant")
    void getById_retourneEditeurExistant() {
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(mapper.toDetailResponse(editeur)).thenReturn(detailResponseDto);

        EditeurDetailResponseDto result = editeurService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Gallimard");
    }

    @Test
    @DisplayName("getById - lève une exception si l'éditeur n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(editeurRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editeurService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne le nouvel éditeur")
    void create_retourneNouvelEditeur() {
        when(mapper.toEntity(createDto)).thenReturn(editeur);
        when(adresseRepo.findById(1)).thenReturn(Optional.of(adresse));
        when(editeurRepo.save(editeur)).thenReturn(editeur);
        when(mapper.toDetailResponse(editeur)).thenReturn(detailResponseDto);

        EditeurDetailResponseDto result = editeurService.create(createDto);

        assertThat(result.getNom()).isEqualTo("Gallimard");
        verify(editeurRepo).save(editeur);
        verify(adresseRepo).findById(1);
    }

    @Test
    @DisplayName("create - lève une exception si l'adresse n'existe pas")
    void create_leveExceptionSiAdresseInexistante() {
        when(mapper.toEntity(createDto)).thenReturn(editeur);
        when(adresseRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editeurService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("1");

        verify(editeurRepo, never()).save(any());
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne l'éditeur modifié")
    void update_retourneEditeurMisAJour() {
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(adresseRepo.findById(1)).thenReturn(Optional.of(adresse));
        when(editeurRepo.save(editeur)).thenReturn(editeur);
        when(mapper.toDetailResponse(editeur)).thenReturn(detailResponseDto);

        EditeurDetailResponseDto result = editeurService.update(1, createDto);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(createDto, editeur);
        verify(editeurRepo).save(editeur);
        verify(adresseRepo).findById(1);
    }

    @Test
    @DisplayName("update - lève une exception si l'éditeur n'existe pas")
    void update_leveExceptionSiEditeurInexistant() {
        when(editeurRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editeurService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(editeurRepo, never()).save(any());
    }

    @Test
    @DisplayName("update - lève une exception si l'adresse n'existe pas")
    void update_leveExceptionSiAdresseInexistante() {
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(adresseRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editeurService.update(1, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("1");

        verify(editeurRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime l'éditeur existant")
    void delete_supprime() {
        when(editeurRepo.existsById(1)).thenReturn(true);

        editeurService.delete(1);

        verify(editeurRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si l'éditeur n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(editeurRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> editeurService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(editeurRepo, never()).deleteById(any());
    }
}