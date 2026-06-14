package com.usmb.but3.td4biblio.service.mock;

import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.LivreMapper;
import com.usmb.but3.td4biblio.repository.*;
import com.usmb.but3.td4biblio.service.LivreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivreServiceTest {

    @Mock private LivreRepo livreRepo;
    @Mock private AuteurRepo auteurRepo;
    @Mock private EditeurRepo editeurRepo;
    @Mock private BibliothequeRepo bibliothequeRepo;
    @Mock private GenreDocumentRepo genreRepo;
    @Mock private CodeRaisonRepo codeRaisonRepo;
    @Mock private LivreMapper mapper;

    private LivreService livreService;

    // ─── Fixtures ────────────────────────────────────────────────────────────

    private Livre livre;
    private LivreCreateDto createDto;
    private LivreResponseDto responseDto;
    private LivreDetailResponseDto detailResponseDto;

    private Auteur auteur;
    private Editeur editeur;
    private Bibliotheque bibliotheque;
    private GenreDocument genre;
    private CodeRaison codeRaison;

    @BeforeEach
    void setUp() {
        livreService = new LivreService(
                livreRepo, mapper,
                auteurRepo, editeurRepo,
                bibliothequeRepo, genreRepo, codeRaisonRepo
        );

        auteur = new Auteur();
        auteur.setId(1);

        editeur = new Editeur();
        editeur.setId(1);

        bibliotheque = new Bibliotheque();
        bibliotheque.setId(1);

        genre = new GenreDocument();
        genre.setId(1);

        codeRaison = new CodeRaison();
        codeRaison.setId(1);

        livre = new Livre();
        livre.setId(1);
        livre.setTitre("Les Misérables");
        livre.setNbPages(1900);
        livre.setCodeIsbn("978-2-07-040850-4");
        livre.setAuteur(auteur);
        livre.setEditeur(editeur);
        livre.setBibliotheque(bibliotheque);
        livre.setGenre(genre);
        livre.setCodeRaison(codeRaison);
        livre.setDatePublication(LocalDate.of(1862, 4, 3));
        livre.setEmpruntable(true);

        createDto = new LivreCreateDto(
//                1, 1, 1, 1, 1,
//                "Les Misérables", null, null, null,
//                LocalDate.now(), LocalDate.of(1862, 4, 3),
//                null, true,
                1900, "978-2-07-040850-4"
        );
        createDto.setAuteurId(1);
        createDto.setEditeurId(1);
        createDto.setBibliothequeId(1);
        createDto.setGenreId(1);
        createDto.setCodeRaisonId(1);
        createDto.setTitre("Les Misérables");
        createDto.setDatePublication(LocalDate.of(1862, 4, 3));

        responseDto = new LivreResponseDto();
        responseDto.setId(1);
        responseDto.setTitre("Les Misérables");

        detailResponseDto = new LivreDetailResponseDto();
        detailResponseDto.setId(1);
        detailResponseDto.setTitre("Les Misérables");
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne la liste de tous les livres")
    void getAll_retourneTousLesLivres() {
        when(livreRepo.findAll()).thenReturn(List.of(livre));
        when(mapper.toResponse(livre)).thenReturn(responseDto);

        List<LivreResponseDto> result = livreService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Les Misérables");
        verify(livreRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun livre")
    void getAll_retourneListeVide() {
        when(livreRepo.findAll()).thenReturn(List.of());

        List<LivreResponseDto> result = livreService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le détail d'un livre existant")
    void getById_retourneLivreExistant() {
        when(livreRepo.findById(1)).thenReturn(Optional.of(livre));
        when(mapper.toDetailResponse(livre)).thenReturn(detailResponseDto);

        LivreDetailResponseDto result = livreService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitre()).isEqualTo("Les Misérables");
    }

    @Test
    @DisplayName("getById - lève une exception si le livre n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(livreRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── create ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create - crée et retourne le nouveau livre")
    void create_retourneNouveauLivre() {
        when(mapper.toEntity(createDto)).thenReturn(livre);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(genreRepo.findById(1)).thenReturn(Optional.of(genre));
        when(codeRaisonRepo.findById(1)).thenReturn(Optional.of(codeRaison));
        when(livreRepo.save(livre)).thenReturn(livre);
        when(mapper.toDetailResponse(livre)).thenReturn(detailResponseDto);

        LivreDetailResponseDto result = livreService.create(createDto);

        assertThat(result.getTitre()).isEqualTo("Les Misérables");
        verify(livreRepo).save(livre);
    }

    @Test
    @DisplayName("create - lève une exception si l'auteur n'existe pas")
    void create_leveExceptionSiAuteurInexistant() {
        when(mapper.toEntity(createDto)).thenReturn(livre);
        when(auteurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Auteur");

        verify(livreRepo, never()).save(any());
    }

    @Test
    @DisplayName("create - lève une exception si l'éditeur n'existe pas")
    void create_leveExceptionSiEditeurInexistant() {
        when(mapper.toEntity(createDto)).thenReturn(livre);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Editeur");

        verify(livreRepo, never()).save(any());
    }

    @Test
    @DisplayName("create - lève une exception si la bibliothèque n'existe pas")
    void create_leveExceptionSiBibliothequeInexistante() {
        when(mapper.toEntity(createDto)).thenReturn(livre);
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreService.create(createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Bibliotheque");

        verify(livreRepo, never()).save(any());
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour et retourne le livre modifié")
    void update_retourneLivreMisAJour() {
        when(livreRepo.findById(1)).thenReturn(Optional.of(livre));
        when(auteurRepo.findById(1)).thenReturn(Optional.of(auteur));
        when(editeurRepo.findById(1)).thenReturn(Optional.of(editeur));
        when(bibliothequeRepo.findById(1)).thenReturn(Optional.of(bibliotheque));
        when(genreRepo.findById(1)).thenReturn(Optional.of(genre));
        when(codeRaisonRepo.findById(1)).thenReturn(Optional.of(codeRaison));
        when(livreRepo.save(livre)).thenReturn(livre);
        when(mapper.toDetailResponse(livre)).thenReturn(detailResponseDto);

        LivreDetailResponseDto result = livreService.update(1, createDto);

        assertThat(result).isNotNull();
        verify(mapper).updateFromDto(createDto, livre);
        verify(livreRepo).save(livre);
    }

    @Test
    @DisplayName("update - lève une exception si le livre n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(livreRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(livreRepo, never()).save(any());
    }

    @Test
    @DisplayName("update - lève une exception si l'auteur n'existe pas")
    void update_leveExceptionSiAuteurInexistant() {
        when(livreRepo.findById(1)).thenReturn(Optional.of(livre));
        when(auteurRepo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreService.update(1, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Auteur");

        verify(livreRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime le livre existant")
    void delete_supprime() {
        when(livreRepo.existsById(1)).thenReturn(true);

        livreService.delete(1);

        verify(livreRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si le livre n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(livreRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> livreService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(livreRepo, never()).deleteById(any());
    }

    // ─── getByAuteurId ────────────────────────────────────────────────────────

    @Test
    @DisplayName("getByAuteurId - retourne les livres d'un auteur triés par id")
    void getByAuteurId_retourneLivres() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(livreRepo.findByAuteurId(1, sort)).thenReturn(List.of(livre));
        when(mapper.toResponse(livre)).thenReturn(responseDto);

        List<LivreResponseDto> result = livreService.getByAuteurId(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Les Misérables");
    }

    @Test
    @DisplayName("getByAuteurId - retourne une liste vide si aucun livre pour cet auteur")
    void getByAuteurId_retourneListeVide() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(livreRepo.findByAuteurId(99, sort)).thenReturn(List.of());

        List<LivreResponseDto> result = livreService.getByAuteurId(99);

        assertThat(result).isEmpty();
    }

    // ─── getByTitreContainingIgnoreCase ───────────────────────────────────────

    @Test
    @DisplayName("getByTitreContainingIgnoreCase - retourne les livres dont le titre contient le filtre")
    void getByTitreContainingIgnoreCase_retourneLivres() {
        when(livreRepo.findByTitreContainingIgnoreCase("misér")).thenReturn(List.of(livre));
        when(mapper.toResponse(livre)).thenReturn(responseDto);

        List<LivreResponseDto> result = livreService.getByTitreContainingIgnoreCase("misér");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("Les Misérables");
    }

    @Test
    @DisplayName("getByTitreContainingIgnoreCase - insensible à la casse")
    void getByTitreContainingIgnoreCase_insensibleCasse() {
        when(livreRepo.findByTitreContainingIgnoreCase("MISÉR")).thenReturn(List.of(livre));
        when(mapper.toResponse(livre)).thenReturn(responseDto);

        List<LivreResponseDto> result = livreService.getByTitreContainingIgnoreCase("MISÉR");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("getByTitreContainingIgnoreCase - retourne une liste vide si aucun résultat")
    void getByTitreContainingIgnoreCase_retourneListeVide() {
        when(livreRepo.findByTitreContainingIgnoreCase("inconnu")).thenReturn(List.of());

        List<LivreResponseDto> result = livreService.getByTitreContainingIgnoreCase("inconnu");

        assertThat(result).isEmpty();
    }
}