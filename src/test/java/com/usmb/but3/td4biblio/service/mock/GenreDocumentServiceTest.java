package com.usmb.but3.td4biblio.service.mock;

import com.usmb.but3.td4biblio.dto.GenreDocumentCreateDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.GenreDocumentMapper;
import com.usmb.but3.td4biblio.repository.GenreDocumentRepo;
import com.usmb.but3.td4biblio.service.GenreDocumentService;
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
class GenreDocumentServiceTest {

    @Mock
    private GenreDocumentRepo genreDocumentRepo;

    @Mock
    private GenreDocumentMapper mapper;

    private GenreDocumentService genreDocumentService;

    private GenreDocument genreDocument;
    private GenreDocumentCreateDto createDto;
    private GenreDocumentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        genreDocumentService = new GenreDocumentService(genreDocumentRepo, mapper);

        genreDocument = new GenreDocument();
        genreDocument.setId(1);
        genreDocument.setNom("Roman");

        createDto = new GenreDocumentCreateDto("Roman");

        responseDto = new GenreDocumentResponseDto();
        responseDto.setId(1);
        responseDto.setNom("Roman");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne tous les genres")
    void getAll_retourneTousLesGenres() {
        when(genreDocumentRepo.findAll()).thenReturn(List.of(genreDocument));
        when(mapper.toResponse(genreDocument)).thenReturn(responseDto);

        List<GenreDocumentResponseDto> result = genreDocumentService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Roman");

        verify(genreDocumentRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide")
    void getAll_retourneListeVide() {
        when(genreDocumentRepo.findAll()).thenReturn(List.of());

        List<GenreDocumentResponseDto> result = genreDocumentService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne un genre existant")
    void getById_retourneGenreExistant() {
        when(genreDocumentRepo.findById(1)).thenReturn(Optional.of(genreDocument));
        when(mapper.toDetailResponse(genreDocument)).thenReturn(responseDto);

        GenreDocumentResponseDto result = genreDocumentService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getNom()).isEqualTo("Roman");
    }

    @Test
    @DisplayName("getById - lève une exception si le genre n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(genreDocumentRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreDocumentService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne un genre")
    void create_retourneNouveauGenre() {
        when(mapper.toEntity(createDto)).thenReturn(genreDocument);
        when(genreDocumentRepo.save(genreDocument)).thenReturn(genreDocument);
        when(mapper.toDetailResponse(genreDocument)).thenReturn(responseDto);

        GenreDocumentResponseDto result = genreDocumentService.create(createDto);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Roman");

        verify(genreDocumentRepo).save(genreDocument);
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne le genre")
    void update_retourneGenreMisAJour() {
        GenreDocumentCreateDto updateDto = new GenreDocumentCreateDto("Science-Fiction");

        when(genreDocumentRepo.findById(1)).thenReturn(Optional.of(genreDocument));
        when(genreDocumentRepo.save(genreDocument)).thenReturn(genreDocument);

        GenreDocumentResponseDto updatedDto =
                new GenreDocumentResponseDto(1, "Science-Fiction");

        when(mapper.toResponse(any(GenreDocument.class))).thenReturn(updatedDto);

        GenreDocumentResponseDto result =
                genreDocumentService.update(1, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Science-Fiction");
        assertThat(genreDocument.getNom()).isEqualTo("Science-Fiction");

        verify(genreDocumentRepo).save(genreDocument);
    }

    @Test
    @DisplayName("update - lève une exception si le genre n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(genreDocumentRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                genreDocumentService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(genreDocumentRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime le genre existant")
    void delete_supprimeGenre() {
        when(genreDocumentRepo.existsById(1)).thenReturn(true);

        genreDocumentService.delete(1);

        verify(genreDocumentRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si le genre n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(genreDocumentRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() ->
                genreDocumentService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(genreDocumentRepo, never()).deleteById(any());
    }
}