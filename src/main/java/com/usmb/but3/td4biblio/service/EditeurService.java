package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.*;
import com.usmb.but3.td4biblio.entity.Adresse;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EditeurMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EditeurService
                extends AbstractGenericService<Editeur, Integer, EditeurResponseDto, EditeurDetailResponseDto, EditeurCreateDto>{

    private final EditeurMapper mapper;
    private final AdresseRepo adresseRepo;

    public EditeurService(EditeurRepo repository, EditeurMapper mapper, AdresseRepo adresseRepo) {
        super(repository, mapper);
        this.mapper = mapper;
        this.adresseRepo = adresseRepo;
    }
    @Override
    public EditeurDetailResponseDto update(Integer id, EditeurCreateDto dto) {
        Editeur editeur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        mapper.updateFromDto(dto, editeur);

        Adresse adresse = adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvée : " + dto.getAdresseId()));

        editeur.setAdresse(adresse);

        return mapper.toDetailResponse(repository.save(editeur));
    }

    @Override
    public EditeurDetailResponseDto create(EditeurCreateDto dto) {
        Editeur editeur = mapper.toEntity(dto);

        editeur.setAdresse(adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvée : " + dto.getAdresseId())));

        return mapper.toDetailResponse(repository.save(editeur));
    }

    public List<EditeurResponseDto> getByNomContainingIgnoreCase(String nom) {
        return ((EditeurRepo) repository).findByNomContainingIgnoreCase(nom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Integer resolveAdresseId(AdresseCreateDto adresseDto) {
        return adresseRepo.findByRueAndCodePostalAndVille(
                        adresseDto.getRue(),
                        adresseDto.getCodePostal(),
                        adresseDto.getVille())
                .map(Adresse::getId)
                .orElseGet(() -> {
                    Adresse nouvelle = new Adresse();
                    nouvelle.setRue(adresseDto.getRue());
                    nouvelle.setCodePostal(adresseDto.getCodePostal());
                    nouvelle.setVille(adresseDto.getVille());
                    return adresseRepo.save(nouvelle).getId();
                });
    }

}
