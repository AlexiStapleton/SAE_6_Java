package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.CodeRaisonCreateDto;
import com.usmb.but3.td4biblio.dto.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.entity.CodeRaison;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.CodeRaisonMapper;
import com.usmb.but3.td4biblio.repository.CodeRaisonRepo;
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
class CodeRaisonServiceTest {

    @Mock private CodeRaisonRepo codeRaisonRepo;
    @Mock private CodeRaisonMapper mapper;

    private CodeRaisonService codeRaisonService;

    private CodeRaison codeRaison;
    private CodeRaisonCreateDto createDto;
    private CodeRaisonResponseDto responseDto;

    @BeforeEach
    void setUp() {
        codeRaisonService = new CodeRaisonService(codeRaisonRepo, mapper);

        codeRaison = new CodeRaison();
        codeRaison.setId(1);
        codeRaison.setNom("Perte");
        codeRaison.setDescription("estPerdue");

        createDto = new CodeRaisonCreateDto("Perte", "estPerdue");

        responseDto = new CodeRaisonResponseDto();
        responseDto.setId(1);
        responseDto.setNom("Perte");
        responseDto.setDescription("estPerdue");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les codes raison")
    void getAll_retourneTousLesCodesRaison() {
        when(codeRaisonRepo.findAll()).thenReturn(List.of(codeRaison));
        when(mapper.toResponse(codeRaison)).thenReturn(responseDto);

        List<CodeRaisonResponseDto> result = codeRaisonService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Perte");
        verify(codeRaisonRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun code raison")
    void getAll_retourneListeVide() {
        when(codeRaisonRepo.findAll()).thenReturn(List.of());

        List<CodeRaisonResponseDto> result = codeRaisonService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le code raison existant")
    void getById_retourneCodeRaisonExistant() {
        when(codeRaisonRepo.findById(1)).thenReturn(Optional.of(codeRaison));
        when(mapper.toDetailResponse(codeRaison)).thenReturn(responseDto);

        CodeRaisonResponseDto result = codeRaisonService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Perte");
    }

    @Test
    @DisplayName("getById - lève une exception si le code raison n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(codeRaisonRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> codeRaisonService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne le nouveau code raison")
    void create_retourneNouveauCodeRaison() {
        when(mapper.toEntity(createDto)).thenReturn(codeRaison);
        when(codeRaisonRepo.save(codeRaison)).thenReturn(codeRaison);
        when(mapper.toDetailResponse(codeRaison)).thenReturn(responseDto);

        CodeRaisonResponseDto result = codeRaisonService.create(createDto);

        assertThat(result.getNom()).isEqualTo("Perte");
        verify(codeRaisonRepo).save(codeRaison);
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne le code raison modifié")
    void update_retourneCodeRaisonMisAJour() {
        when(codeRaisonRepo.findById(1)).thenReturn(Optional.of(codeRaison));
        when(codeRaisonRepo.save(codeRaison)).thenReturn(codeRaison);
        when(mapper.toDetailResponse(codeRaison)).thenReturn(responseDto);

        CodeRaisonResponseDto result = codeRaisonService.update(1, createDto);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(createDto, codeRaison);
        verify(codeRaisonRepo).save(codeRaison);
    }

    @Test
    @DisplayName("update - lève une exception si le code raison n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(codeRaisonRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> codeRaisonService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(codeRaisonRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime le code raison existant")
    void delete_supprime() {
        when(codeRaisonRepo.existsById(1)).thenReturn(true);

        codeRaisonService.delete(1);

        verify(codeRaisonRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si le code raison n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(codeRaisonRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> codeRaisonService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(codeRaisonRepo, never()).deleteById(any());
    }
}