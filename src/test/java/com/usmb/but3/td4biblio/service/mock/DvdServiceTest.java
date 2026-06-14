package com.usmb.but3.td4biblio.service.mock;

import com.usmb.but3.td4biblio.dto.DvdCreateDto;
import com.usmb.but3.td4biblio.dto.DvdDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DvdResponseDto;
import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.DvdMapper;
import com.usmb.but3.td4biblio.repository.*;
import com.usmb.but3.td4biblio.service.DvdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class DvdServiceTest {

    @Mock private DvdRepo dvdRepo;
    @Mock private AuteurRepo auteurRepo;
    @Mock private EditeurRepo editeurRepo;
    @Mock private BibliothequeRepo bibliothequeRepo;
    @Mock private GenreDocumentRepo genreRepo;
    @Mock private CodeRaisonRepo codeRaisonRepo;
    @Mock private DvdMapper mapper;

    private DvdService dvdService;

    private Dvd dvd;
    private DvdCreateDto createDto;
    private DvdResponseDto responseDto;
    private DvdDetailResponseDto detailResponseDto;

    private Auteur auteur;
    private Editeur editeur;
    private Bibliotheque bibliotheque;
    private GenreDocument genre;
    private CodeRaison codeRaison;

    @BeforeEach
    void setUp() {
        dvdService = new DvdService(
                dvdRepo, mapper,
                auteurRepo, editeurRepo,
                bibliothequeRepo, genreRepo, codeRaisonRepo
        );

        auteur = new Auteur(); auteur.setId(1);
        editeur = new Editeur(); editeur.setId(1);
        bibliotheque = new Bibliotheque(); bibliotheque.setId(1);
        genre = new GenreDocument(); genre.setId(1);
        codeRaison = new CodeRaison(); codeRaison.setId(1);

        dvd = new Dvd();
        dvd.setId(1);
        dvd.setTitre("Inception");
        dvd.setDuree(148);
        dvd.setAuteur(auteur);
        dvd.setEditeur(editeur);
        dvd.setBibliotheque(bibliotheque);
        dvd.setGenre(genre);
        dvd.setCodeRaison(codeRaison);
        dvd.setDatePublication(LocalDate.of(2010, 7, 16));
        dvd.setEmpruntable(true);

        createDto = new DvdCreateDto();
        createDto.setAuteurId(1);
        createDto.setEditeurId(1);
        createDto.setBibliothequeId(1);
        createDto.setGenreId(1);
        createDto.setCodeRaisonId(1);
        createDto.setTitre("Inception");
        createDto.setDuree(148);

        responseDto = new DvdResponseDto();
        responseDto.setId(1);
        responseDto.setTitre("Inception");

        detailResponseDto = new DvdDetailResponseDto();
        detailResponseDto.setId(1);
        detailResponseDto.setTitre("Inception");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les dvds")
    void getAll_retourneTousLesDvds() {
        when(dvdRepo.findAll()).thenReturn(List.of(dvd));
        when(mapper.toResponse(dvd)).thenReturn(responseDto);

        List<DvdResponseDto> result = dvdService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Inception");
        verify(dvdRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun dvd")
    void getAll_retourneListeVide() {
        when(dvdRepo.findAll()).thenReturn(List.of());

        List<DvdResponseDto> result = dvdService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'un dvd existant")
    void getById_retourneDvdExistant() {
        when(dvdRepo.findById(1)).thenReturn(Optional.of(dvd));
        when(mapper.toDetailResponse(dvd)).thenReturn(detailResponseDto);

        DvdDetailResponseDto result = dvdService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitre()).isEqualTo("Inception");
    }

    @Test
    @DisplayName("getById - lève une exception si le dvd n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(dvdRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dvdService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne le nouveau dvd")
    void create_retourneNouveauDvd() {
        when(mapper.toEntity(createDto)).thenReturn(dvd);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(genreRepo.findById(1)).thenReturn(Optional.of(genre));
        when(codeRaisonRepo.findById(1)).thenReturn(Optional.of(codeRaison));
        when(dvdRepo.save(dvd)).thenReturn(dvd);
        when(mapper.toDetailResponse(dvd)).thenReturn(detailResponseDto);

        DvdDetailResponseDto result = dvdService.create(createDto);

        assertThat(result.getTitre()).isEqualTo("Inception");
        verify(dvdRepo).save(dvd);
    }

    @Test
    @DisplayName("create - lève une exception si l'auteur n'existe pas")
    void create_leveExceptionSiAuteurInexistant() {
        when(mapper.toEntity(createDto)).thenReturn(dvd);
        when(auteurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dvdService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Auteur");

        verify(dvdRepo, never()).save(any());
    }

    @Test
    @DisplayName("create - lève une exception si l'éditeur n'existe pas")
    void create_leveExceptionSiEditeurInexistant() {
        when(mapper.toEntity(createDto)).thenReturn(dvd);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dvdService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Editeur");

        verify(dvdRepo, never()).save(any());
    }

    @Test
    @DisplayName("create - lève une exception si la bibliothèque n'existe pas")
    void create_leveExceptionSiBibliothequeInexistante() {
        when(mapper.toEntity(createDto)).thenReturn(dvd);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dvdService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Bibliotheque");

        verify(dvdRepo, never()).save(any());
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne le dvd modifié")
    void update_retourneDvdMisAJour() {
        when(dvdRepo.findById(1)).thenReturn(Optional.of(dvd));
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(genreRepo.findById(1)).thenReturn(Optional.of(genre));
        when(codeRaisonRepo.findById(1)).thenReturn(Optional.of(codeRaison));
        when(dvdRepo.save(dvd)).thenReturn(dvd);
        when(mapper.toDetailResponse(dvd)).thenReturn(detailResponseDto);

        DvdDetailResponseDto result = dvdService.update(1, createDto);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(createDto, dvd);
        verify(dvdRepo).save(dvd);
    }

    @Test
    @DisplayName("update - lève une exception si le dvd n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(dvdRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dvdService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(dvdRepo, never()).save(any());
    }

    @Test
    @DisplayName("update - lève une exception si l'auteur n'existe pas")
    void update_leveExceptionSiAuteurInexistant() {
        when(dvdRepo.findById(1)).thenReturn(Optional.of(dvd));
        when(auteurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dvdService.update(1, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Auteur");

        verify(dvdRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime le dvd existant")
    void delete_supprime() {
        when(dvdRepo.existsById(1)).thenReturn(true);

        dvdService.delete(1);

        verify(dvdRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si le dvd n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(dvdRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> dvdService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(dvdRepo, never()).deleteById(any());
    }
}