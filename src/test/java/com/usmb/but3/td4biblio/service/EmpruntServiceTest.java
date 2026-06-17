package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EmpruntCreateDto;
import com.usmb.but3.td4biblio.dto.EmpruntDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EmpruntResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EmpruntMapper;
import com.usmb.but3.td4biblio.mapper.UtilisateurRepo;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmpruntRepo;
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
class EmpruntServiceTest {

    @Mock private EmpruntRepo empruntRepo;
    @Mock private UtilisateurRepo utilisateurRepo;
    @Mock private DocumentRepo documentRepo;
    @Mock private EmpruntMapper mapper;

    private EmpruntService empruntService;

    private Emprunt emprunt;
    private Emprunt.EmpruntId empruntId;
    private EmpruntCreateDto createDto;
    private EmpruntResponseDto responseDto;
    private EmpruntDetailResponseDto detailResponseDto;
    private Utilisateur utilisateur;
    private Document document;

    @BeforeEach
    void setUp() {
        empruntService = new EmpruntService(empruntRepo, mapper, utilisateurRepo, documentRepo);

        utilisateur = new Utilisateur();
        utilisateur.setId(1);

        document = new Document();
        document.setId(1);

        empruntId = new Emprunt.EmpruntId(1, 1);

        emprunt = new Emprunt();
        emprunt.setId(empruntId);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDocument(document);
        emprunt.setDateCreation(LocalDate.now());

        createDto = new EmpruntCreateDto(1, 1, null);

        responseDto = new EmpruntResponseDto();
        responseDto.setId(empruntId);

        detailResponseDto = new EmpruntDetailResponseDto();
        detailResponseDto.setId(empruntId);
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les emprunts")
    void getAll_retourneTousLesEmprunts() {
        when(empruntRepo.findAll()).thenReturn(List.of(emprunt));
        when(mapper.toResponse(emprunt)).thenReturn(responseDto);

        List<EmpruntResponseDto> result = empruntService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(empruntId);
        verify(empruntRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun emprunt")
    void getAll_retourneListeVide() {
        when(empruntRepo.findAll()).thenReturn(List.of());

        List<EmpruntResponseDto> result = empruntService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'un emprunt existant")
    void getById_retourneEmpruntExistant() {
        when(empruntRepo.findById(empruntId)).thenReturn(Optional.of(emprunt));
        when(mapper.toDetailResponse(emprunt)).thenReturn(detailResponseDto);

        EmpruntDetailResponseDto result = empruntService.getById(empruntId);

        assertThat(result.getId()).isEqualTo(empruntId);
    }

    @Test
    @DisplayName("getById - lève une exception si l'emprunt n'existe pas")
    void getById_leveExceptionSiInexistant() {
        Emprunt.EmpruntId inexistant = new Emprunt.EmpruntId(99, 99);
        when(empruntRepo.findById(inexistant)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.getById(inexistant))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne le nouvel emprunt")
    void create_retourneNouvelEmprunt() {
        when(mapper.toEntity(createDto)).thenReturn(emprunt);
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(utilisateurRepo.findById(1)).thenReturn(Optional.of(utilisateur));
        when(empruntRepo.save(emprunt)).thenReturn(emprunt);
        when(mapper.toDetailResponse(emprunt)).thenReturn(detailResponseDto);

        EmpruntDetailResponseDto result = empruntService.create(createDto);

        assertThat(result.getId()).isEqualTo(empruntId);
        verify(empruntRepo).save(emprunt);
    }

    @Test
    @DisplayName("create - lève une exception si le document n'existe pas")
    void create_leveExceptionSiDocumentInexistant() {
        when(mapper.toEntity(createDto)).thenReturn(emprunt);
        when(documentRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Document");

        verify(empruntRepo, never()).save(any());
    }

    @Test
    @DisplayName("create - lève une exception si l'utilisateur n'existe pas")
    void create_leveExceptionSiUtilisateurInexistant() {
        when(mapper.toEntity(createDto)).thenReturn(emprunt);
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(utilisateurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class);

        verify(empruntRepo, never()).save(any());
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne l'emprunt modifié")
    void update_retourneEmpruntMisAJour() {
        when(empruntRepo.findById(empruntId)).thenReturn(Optional.of(emprunt));
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(utilisateurRepo.findById(1)).thenReturn(Optional.of(utilisateur));
        when(empruntRepo.save(emprunt)).thenReturn(emprunt);
        when(mapper.toDetailResponse(emprunt)).thenReturn(detailResponseDto);

        EmpruntDetailResponseDto result = empruntService.update(empruntId, createDto);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(createDto, emprunt);
        verify(empruntRepo).save(emprunt);
    }

    @Test
    @DisplayName("update - lève une exception si l'emprunt n'existe pas")
    void update_leveExceptionSiInexistant() {
        Emprunt.EmpruntId inexistant = new Emprunt.EmpruntId(99, 99);
        when(empruntRepo.findById(inexistant)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.update(inexistant, createDto))
                .isInstanceOf(RessourceNotFoundException.class);

        verify(empruntRepo, never()).save(any());
    }

    @Test
    @DisplayName("update - lève une exception si le document n'existe pas")
    void update_leveExceptionSiDocumentInexistant() {
        when(empruntRepo.findById(empruntId)).thenReturn(Optional.of(emprunt));
        when(documentRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.update(empruntId, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Document");

        verify(empruntRepo, never()).save(any());
    }

    @Test
    @DisplayName("update - lève une exception si l'utilisateur n'existe pas")
    void update_leveExceptionSiUtilisateurInexistant() {
        when(empruntRepo.findById(empruntId)).thenReturn(Optional.of(emprunt));
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(utilisateurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.update(empruntId, createDto))
                .isInstanceOf(RessourceNotFoundException.class);

        verify(empruntRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime l'emprunt existant")
    void delete_supprime() {
        when(empruntRepo.existsById(empruntId)).thenReturn(true);

        empruntService.delete(empruntId);

        verify(empruntRepo).deleteById(empruntId);
    }

    @Test
    @DisplayName("delete - lève une exception si l'emprunt n'existe pas")
    void delete_leveExceptionSiInexistant() {
        Emprunt.EmpruntId inexistant = new Emprunt.EmpruntId(99, 99);
        when(empruntRepo.existsById(inexistant)).thenReturn(false);

        assertThatThrownBy(() -> empruntService.delete(inexistant))
                .isInstanceOf(RessourceNotFoundException.class);

        verify(empruntRepo, never()).deleteById(any());
    }
}