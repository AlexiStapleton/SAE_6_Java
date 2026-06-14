package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.BibliothequeMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;
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
class BibliothequeServiceTest {

    @Mock private BibliothequeRepo bibliothequeRepo;
    @Mock private AdresseRepo adresseRepo;
    @Mock private BibliothequeMapper mapper;

    private BibliothequeService bibliothequeService;

    private Bibliotheque bibliotheque;
    private BibliothequeCreateDto createDto;
    private BibliothequeResponseDto responseDto;
    private BibliothequeDetailResponseDto detailResponseDto;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        bibliothequeService = new BibliothequeService(bibliothequeRepo, mapper, adresseRepo);

        adresse = new Adresse();
        adresse.setId(1);

        bibliotheque = new Bibliotheque();
        bibliotheque.setId(1);
        bibliotheque.setNom("Bibliothèque Municipale");
        bibliotheque.setAdresse(adresse);

        createDto = new BibliothequeCreateDto("Bibliothèque Municipale", 1);

        responseDto = new BibliothequeResponseDto();
        responseDto.setId(1);
        responseDto.setNom("Bibliothèque Municipale");

        detailResponseDto = new BibliothequeDetailResponseDto();
        detailResponseDto.setId(1);
        detailResponseDto.setNom("Bibliothèque Municipale");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de toutes les bibliothèques")
    void getAll_retourneToutesLesBibliotheques() {
        when(bibliothequeRepo.findAll()).thenReturn(List.of(bibliotheque));
        when(mapper.toResponse(bibliotheque)).thenReturn(responseDto);

        List<BibliothequeResponseDto> result = bibliothequeService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Bibliothèque Municipale");
        verify(bibliothequeRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucune bibliothèque")
    void getAll_retourneListeVide() {
        when(bibliothequeRepo.findAll()).thenReturn(List.of());

        List<BibliothequeResponseDto> result = bibliothequeService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'une bibliothèque existante")
    void getById_retourneBibliothequeExistante() {
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(mapper.toDetailResponse(bibliotheque)).thenReturn(detailResponseDto);

        BibliothequeDetailResponseDto result = bibliothequeService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Bibliothèque Municipale");
    }

    @Test
    @DisplayName("getById - lève une exception si la bibliothèque n'existe pas")
    void getById_leveExceptionSiInexistante() {
        when(bibliothequeRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bibliothequeService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne la nouvelle bibliothèque")
    void create_retourneNouvelleBibliotheque() {
        when(mapper.toEntity(createDto)).thenReturn(bibliotheque);
        when(adresseRepo.findById(1)).thenReturn(Optional.of(adresse));
        when(bibliothequeRepo.save(bibliotheque)).thenReturn(bibliotheque);
        when(mapper.toDetailResponse(bibliotheque)).thenReturn(detailResponseDto);

        BibliothequeDetailResponseDto result = bibliothequeService.create(createDto);

        assertThat(result.getNom()).isEqualTo("Bibliothèque Municipale");
        verify(bibliothequeRepo).save(bibliotheque);
    }

    @Test
    @DisplayName("create - lève une exception si l'adresse n'existe pas")
    void create_leveExceptionSiAdresseInexistante() {
        when(mapper.toEntity(createDto)).thenReturn(bibliotheque);
        when(adresseRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bibliothequeService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Adresse");

        verify(bibliothequeRepo, never()).save(any());
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne la bibliothèque modifiée")
    void update_retourneBibliothequeMiseAJour() {
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(adresseRepo.findById(1)).thenReturn(Optional.of(adresse));
        when(bibliothequeRepo.save(bibliotheque)).thenReturn(bibliotheque);
        when(mapper.toDetailResponse(bibliotheque)).thenReturn(detailResponseDto);

        BibliothequeDetailResponseDto result = bibliothequeService.update(1, createDto);

        assertThat(result).isNotNull();
        assertThat(bibliotheque.getNom()).isEqualTo("Bibliothèque Municipale");
        verify(bibliothequeRepo).save(bibliotheque);
    }

    @Test
    @DisplayName("update - lève une exception si la bibliothèque n'existe pas")
    void update_leveExceptionSiInexistante() {
        when(bibliothequeRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bibliothequeService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(bibliothequeRepo, never()).save(any());
    }

    @Test
    @DisplayName("update - lève une exception si l'adresse n'existe pas")
    void update_leveExceptionSiAdresseInexistante() {
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(adresseRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bibliothequeService.update(1, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Adresse");

        verify(bibliothequeRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime la bibliothèque existante")
    void delete_supprime() {
        when(bibliothequeRepo.existsById(1)).thenReturn(true);

        bibliothequeService.delete(1);

        verify(bibliothequeRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si la bibliothèque n'existe pas")
    void delete_leveExceptionSiInexistante() {
        when(bibliothequeRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> bibliothequeService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(bibliothequeRepo, never()).deleteById(any());
    }
}