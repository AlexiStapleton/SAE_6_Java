package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Dvd;
import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.enumeration.ChampRechercheDocument;
import com.usmb.but3.td4biblio.enumeration.TypeDocument;
import com.usmb.but3.td4biblio.enumeration.TypeRecherche;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.DocumentMapper;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.DvdRepo;
import com.usmb.but3.td4biblio.repository.LivreRepo;
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
class DocumentServiceTest {

    @Mock private DocumentRepo documentRepo;
    @Mock private LivreRepo livreRepo;
    @Mock private DvdRepo dvdRepo;
    @Mock private DocumentMapper mapper;

    private DocumentService documentService;

    private Document document;
    private DocumentCreateDto createDto;
    private DocumentResponseDto responseDto;
    private DocumentDetailResponseDto detailResponseDto;

    @BeforeEach
    void setUp() {
        documentService = new DocumentService(documentRepo, mapper, livreRepo, dvdRepo);

        document = new Document();
        document.setId(1);
        document.setTitre("Les Misérables");
        document.setEmpruntable(true);
        document.setDateAcquisition(LocalDate.of(2020, 1, 1));

        createDto = new DocumentCreateDto();
        createDto.setTitre("Les Misérables — éd. révisée");
        createDto.setGif("cover.gif");
        createDto.setFormat("Broché");
        createDto.setDescription("Un classique");
        createDto.setDateAcquisition(LocalDate.of(2024, 6, 1));
        createDto.setDatePublication(LocalDate.of(1862, 1, 1));
        createDto.setCodeEmplacement("A1");
        createDto.setEmpruntable(true);

        responseDto = new DocumentResponseDto();
        responseDto.setId(1);

        detailResponseDto = new DocumentDetailResponseDto();
        detailResponseDto.setId(1);
    }

    // ─── getAll() ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll - retourne tous les documents")
    void getAll_retourneTousLesDocuments() {
        when(documentRepo.findAll()).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        verify(documentRepo).findAll();
    }

    @Test
    @DisplayName("getAll - retourne une liste vide si aucun document")
    void getAll_retourneListeVide() {
        when(documentRepo.findAll()).thenReturn(List.of());

        List<DocumentResponseDto> result = documentService.getAll();

        assertThat(result).isEmpty();
    }

    // ─── getAll(TypeDocument) ─────────────────────────────────────────────────

    @Test
    @DisplayName("getAll(null) - retourne tous les documents sans filtre")
    void getAllAvecType_null_retourneTous() {
        when(documentRepo.findAll()).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.getAll(null);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("getAll(LIVRE) - retourne uniquement les livres")
    void getAllAvecType_livre_retourneLivres() {
        Livre livre = new Livre();
        livre.setId(1);
        when(livreRepo.findAll()).thenReturn(List.of(livre));
        when(mapper.toResponse(livre)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.getAll(TypeDocument.LIVRE);

        assertThat(result).hasSize(1);
        verify(livreRepo).findAll();
        verify(dvdRepo, never()).findAll();
    }

    @Test
    @DisplayName("getAll(DVD) - retourne uniquement les DVDs")
    void getAllAvecType_dvd_retourneDvds() {
        Dvd dvd = new Dvd();
        dvd.setId(2);
        when(dvdRepo.findAll()).thenReturn(List.of(dvd));
        when(mapper.toResponse(dvd)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.getAll(TypeDocument.DVD);

        assertThat(result).hasSize(1);
        verify(dvdRepo).findAll();
        verify(livreRepo, never()).findAll();
    }

    // ─── getById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById - retourne le document avec type LIVRE")
    void getById_retourneDocumentAvecTypeLivre() {
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(mapper.toDetailResponse(document)).thenReturn(detailResponseDto);
        when(livreRepo.existsById(1)).thenReturn(true);

        DocumentDetailResponseDto result = documentService.getById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getType()).isEqualTo(TypeDocument.LIVRE);
    }

    @Test
    @DisplayName("getById - retourne le document avec type DVD")
    void getById_retourneDocumentAvecTypeDvd() {
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(mapper.toDetailResponse(document)).thenReturn(detailResponseDto);
        when(livreRepo.existsById(1)).thenReturn(false);
        when(dvdRepo.existsById(1)).thenReturn(true);

        DocumentDetailResponseDto result = documentService.getById(1);

        assertThat(result.getType()).isEqualTo(TypeDocument.DVD);
    }

    @Test
    @DisplayName("getById - type non défini si ni livre ni DVD")
    void getById_typeNonDefiniSiDocumentGenerique() {
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(mapper.toDetailResponse(document)).thenReturn(detailResponseDto);
        when(livreRepo.existsById(1)).thenReturn(false);
        when(dvdRepo.existsById(1)).thenReturn(false);

        DocumentDetailResponseDto result = documentService.getById(1);

        assertThat(result.getType()).isNull();
    }

    @Test
    @DisplayName("getById - lève une exception si le document n'existe pas")
    void getById_leveExceptionSiInexistant() {
        when(documentRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.getById(99))
                .isInstanceOf(RessourceNotFoundException.class);
    }

    // ─── getEntityById ────────────────────────────────────────────────────────

    @Test
    @DisplayName("getEntityById - retourne l'entité Document")
    void getEntityById_retourneEntite() {
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));

        Document result = documentService.getEntityById(1);

        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("getEntityById - lève une exception si inexistant")
    void getEntityById_leveExceptionSiInexistant() {
        when(documentRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.getEntityById(99))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update - met à jour les champs et retourne le détail")
    void update_metsAJourLeDocument() {
        when(documentRepo.findById(1)).thenReturn(Optional.of(document));
        when(documentRepo.save(document)).thenReturn(document);
        when(mapper.toDetailResponse(document)).thenReturn(detailResponseDto);

        DocumentDetailResponseDto result = documentService.update(1, createDto);

        assertThat(result).isNotNull();
        assertThat(document.getTitre()).isEqualTo("Les Misérables — éd. révisée");
        assertThat(document.getEmpruntable()).isTrue();
        verify(documentRepo).save(document);
    }

    @Test
    @DisplayName("update - lève une exception si le document n'existe pas")
    void update_leveExceptionSiInexistant() {
        when(documentRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.update(99, createDto))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(documentRepo, never()).save(any());
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete - supprime le document existant")
    void delete_supprimeLeDocument() {
        when(documentRepo.existsById(1)).thenReturn(true);

        documentService.delete(1);

        verify(documentRepo).deleteById(1);
    }

    @Test
    @DisplayName("delete - lève une exception si le document n'existe pas")
    void delete_leveExceptionSiInexistant() {
        when(documentRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> documentService.delete(99))
                .isInstanceOf(RessourceNotFoundException.class);

        verify(documentRepo, never()).deleteById(any());
    }

    // ─── getNewAcquisitions ───────────────────────────────────────────────────

    @Test
    @DisplayName("getNewAcquisitions - retourne les 5 documents les plus récents")
    void getNewAcquisitions_retourneTop5() {
        when(documentRepo.findTop5ByOrderByDateAcquisitionDesc()).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.getNewAcquisitions();

        assertThat(result).hasSize(1);
        verify(documentRepo).findTop5ByOrderByDateAcquisitionDesc();
    }

    @Test
    @DisplayName("getNewAcquisitions - retourne une liste vide si aucun document")
    void getNewAcquisitions_retourneListeVide() {
        when(documentRepo.findTop5ByOrderByDateAcquisitionDesc()).thenReturn(List.of());

        List<DocumentResponseDto> result = documentService.getNewAcquisitions();

        assertThat(result).isEmpty();
    }

    // ─── searchDocuments ──────────────────────────────────────────────────────

    @Test
    @DisplayName("search TITRE / CONTIENT - retourne les documents correspondants")
    void search_titre_contient() {
        when(documentRepo.findByTitreContainingIgnoreCase("misér")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.TITRE, TypeRecherche.CONTIENT, "misér");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search TITRE / COMMENCE_PAR - retourne les documents correspondants")
    void search_titre_commencePar() {
        when(documentRepo.findByTitreStartsWithIgnoreCase("Les")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.TITRE, TypeRecherche.COMMENCE_PAR, "Les");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search TITRE / EGAL - retourne les documents correspondants")
    void search_titre_egal() {
        when(documentRepo.findByTitreIgnoreCase("Les Misérables")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.TITRE, TypeRecherche.EGAL, "Les Misérables");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search AUTEUR / CONTIENT")
    void search_auteur_contient() {
        when(documentRepo.findByAuteurNomContainingIgnoreCase("Hugo")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.AUTEUR, TypeRecherche.CONTIENT, "Hugo");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search AUTEUR / COMMENCE_PAR")
    void search_auteur_commencePar() {
        when(documentRepo.findByAuteurNomStartsWithIgnoreCase("Hu")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.AUTEUR, TypeRecherche.COMMENCE_PAR, "Hu");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search AUTEUR / EGAL")
    void search_auteur_egal() {
        when(documentRepo.findByAuteurNomIgnoreCase("Hugo")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.AUTEUR, TypeRecherche.EGAL, "Hugo");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search EDITEUR / CONTIENT")
    void search_editeur_contient() {
        when(documentRepo.findByEditeurNomContainingIgnoreCase("Garnier")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.EDITEUR, TypeRecherche.CONTIENT, "Garnier");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search EDITEUR / COMMENCE_PAR")
    void search_editeur_commencePar() {
        when(documentRepo.findByEditeurNomStartsWithIgnoreCase("Gar")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.EDITEUR, TypeRecherche.COMMENCE_PAR, "Gar");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search EDITEUR / EGAL")
    void search_editeur_egal() {
        when(documentRepo.findByEditeurNomIgnoreCase("Garnier")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.EDITEUR, TypeRecherche.EGAL, "Garnier");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search BIBLIOTHEQUE / CONTIENT")
    void search_bibliotheque_contient() {
        when(documentRepo.findByBibliothequeNomContainingIgnoreCase("Cham")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.BIBLIOTHEQUE, TypeRecherche.CONTIENT, "Cham");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search BIBLIOTHEQUE / COMMENCE_PAR")
    void search_bibliotheque_commencePar() {
        when(documentRepo.findByBibliothequeNomStartsWithIgnoreCase("Bib")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.BIBLIOTHEQUE, TypeRecherche.COMMENCE_PAR, "Bib");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search BIBLIOTHEQUE / EGAL")
    void search_bibliotheque_egal() {
        when(documentRepo.findByBibliothequeNomIgnoreCase("Biblio Chambéry")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.BIBLIOTHEQUE, TypeRecherche.EGAL, "Biblio Chambéry");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search GENRE / CONTIENT")
    void search_genre_contient() {
        when(documentRepo.findByGenreNomContainingIgnoreCase("Rom")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.GENRE, TypeRecherche.CONTIENT, "Rom");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search GENRE / COMMENCE_PAR")
    void search_genre_commencePar() {
        when(documentRepo.findByGenreNomStartsWithIgnoreCase("Ro")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.GENRE, TypeRecherche.COMMENCE_PAR, "Ro");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search GENRE / EGAL")
    void search_genre_egal() {
        when(documentRepo.findByGenreNomIgnoreCase("Roman")).thenReturn(List.of(document));
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.GENRE, TypeRecherche.EGAL, "Roman");

        assertThat(result).hasSize(1);
    }

    // ─── searchDocuments avec filtre TypeDocument ─────────────────────────────

    @Test
    @DisplayName("search avec filtre LIVRE - ne retourne que les livres")
    void search_avecFiltreTypeLivre() {
        when(documentRepo.findByTitreContainingIgnoreCase("Les")).thenReturn(List.of(document));
        when(livreRepo.existsById(1)).thenReturn(true);
        when(mapper.toResponse(document)).thenReturn(responseDto);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.TITRE, TypeRecherche.CONTIENT, "Les", TypeDocument.LIVRE);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("search avec filtre DVD - exclut les livres")
    void search_avecFiltreTypeDvd_exclutLivres() {
        when(documentRepo.findByTitreContainingIgnoreCase("Les")).thenReturn(List.of(document));
        when(dvdRepo.existsById(1)).thenReturn(false);

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.TITRE, TypeRecherche.CONTIENT, "Les", TypeDocument.DVD);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("search - retourne liste vide si aucun résultat")
    void search_retourneListeVideSiAucunResultat() {
        when(documentRepo.findByTitreContainingIgnoreCase("xyz")).thenReturn(List.of());

        List<DocumentResponseDto> result = documentService.searchDocuments(
                ChampRechercheDocument.TITRE, TypeRecherche.CONTIENT, "xyz");

        assertThat(result).isEmpty();
    }
}