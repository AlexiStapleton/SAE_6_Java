package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.DTO.EditeurCreateDto;
import com.usmb.but3.td4biblio.DTO.EditeurResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EditeurMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class EditeurService
        extends AbstractGenericService<Editeur, Integer, EditeurResponseDto, EditeurResponseDto, EditeurCreateDto> {

    private final EditeurRepo editeurRepo;
    private final AdresseRepo adresseRepo;

    public EditeurService(EditeurRepo repository, EditeurMapper mapper, AdresseRepo adresseRepo) {
        super(repository, mapper);
        this.editeurRepo = repository;
        this.adresseRepo = adresseRepo;
    }

    @Override
    public EditeurResponseDto create(EditeurCreateDto dto) {
        Editeur editeur = new Editeur();
        editeur.setNom(dto.getNom());
        editeur.setLienSiteWeb(dto.getLienSiteWeb());
        editeur.setLienWikipedia(dto.getLienWikipedia());
        editeur.setAdresse(resolveAdresse(dto, null));
        return mapper.toResponse(editeurRepo.save(editeur));
    }

    @Override
    public EditeurResponseDto update(Integer id, EditeurCreateDto dto) {
        Editeur editeur = editeurRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        editeur.setNom(dto.getNom());
        editeur.setLienSiteWeb(dto.getLienSiteWeb());
        editeur.setLienWikipedia(dto.getLienWikipedia());
        editeur.setAdresse(resolveAdresse(dto, editeur.getAdresse()));

        return mapper.toResponse(editeurRepo.save(editeur));
    }

    public List<EditeurResponseDto> getByNomContainingIgnoreCase(String nom) {
        return editeurRepo.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Met à jour ou crée une adresse à partir des champs du DTO.
     * Si aucun champ adresse n'est renseigné, on conserve l'adresse existante.
     */
    private Adresse resolveAdresse(EditeurCreateDto dto, Adresse existing) {
        boolean hasAdresseData = StringUtils.hasText(dto.getRue())
                || StringUtils.hasText(dto.getCodePostal())
                || StringUtils.hasText(dto.getVille());

        if (!hasAdresseData) {
            return existing;
        }

        Adresse adresse = existing != null ? existing : new Adresse();
        adresse.setRue(dto.getRue());
        adresse.setCodePostal(dto.getCodePostal());
        adresse.setVille(dto.getVille());
        return adresseRepo.save(adresse);
    }
}