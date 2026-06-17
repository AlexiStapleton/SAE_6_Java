package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.DTO.AdresseCreateDto;
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
        Editeur editeur = mapper.toEntity(dto);
        return mapper.toResponse(editeurRepo.save(editeur));
    }

    @Override
    public EditeurResponseDto update(Integer id, EditeurCreateDto dto) {
        Editeur editeur = editeurRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        editeur.setNom(dto.getNom());
        editeur.setLienSiteWeb(dto.getLienSiteWeb());
        editeur.setLienWikipedia(dto.getLienWikipedia());
        editeur.setAdresse(resolveAdresse(dto.getAdresse(), editeur.getAdresse()));

        return mapper.toResponse(editeurRepo.save(editeur));
    }

    public List<EditeurResponseDto> getByNomContainingIgnoreCase(String nom) {
        return editeurRepo.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    private Adresse resolveAdresse(AdresseCreateDto adresseDto, Adresse existing) {
        if (adresseDto == null) {
            return existing;
        }

        boolean hasAdresseData = StringUtils.hasText(adresseDto.getRue())
                || StringUtils.hasText(adresseDto.getCodePostal())
                || StringUtils.hasText(adresseDto.getVille());

        if (!hasAdresseData) {
            return existing;
        }

        Adresse adresse = existing != null ? existing : new Adresse();
        adresse.setRue(adresseDto.getRue());
        adresse.setCodePostal(adresseDto.getCodePostal());
        adresse.setVille(adresseDto.getVille());
        return adresseRepo.save(adresse);
    }
}