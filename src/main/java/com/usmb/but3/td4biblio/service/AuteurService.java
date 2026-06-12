package com.usmb.but3.td4biblio.service;

import java.util.List;

import com.usmb.but3.td4biblio.dto.AuteurCreateDto;
import com.usmb.but3.td4biblio.dto.AuteurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.AuteurMapper;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.repository.AuteurRepo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
public class AuteurService
    extends AbstractGenericService<Auteur, Integer, AuteurResponseDto, AuteurDetailResponseDto, AuteurCreateDto>{

    private final AuteurRepo auteurRepo;  // ← version typée en plus
    private final TypeAuteurRepo typeAuteurRepo;
    private final AuteurMapper mapper;

    public AuteurService(AuteurRepo repository, AuteurMapper mapper, TypeAuteurRepo typeAuteurRepo) {
        super(repository, mapper);
        this.auteurRepo = repository;  // ← même instance, juste typée correctement
        this.typeAuteurRepo = typeAuteurRepo;
        this.mapper = mapper;
    }

    @Override
    public AuteurDetailResponseDto update(Integer id,  AuteurCreateDto dto) {

        Auteur auteur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));

        mapper.updateFromDto(dto, auteur);

        List<TypeAuteur> types = typeAuteurRepo.findAllById(dto.getTypesAuteurIds());
        auteur.setTypesAuteur(types);

        return mapper.toDetailResponse(repository.save(auteur));
    }

    public List<AuteurResponseDto> getAuteursByNom(String nom) {

        return auteurRepo.findByNom(nom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
    public List<AuteurResponseDto> getAuteursByNomAndPrenom(String nom, String prenom) {
        return auteurRepo.findByNomAndPrenom(nom, prenom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
    public List<AuteurResponseDto> getAuteursByNomLikeAndPrenomLike(String nom, String prenom) {
        //return auteurRepo.findByNomLikeAndPrenomLike("%" + nom + "%", "%" + prenom + "%");
        return auteurRepo.findByNomLikeAndPrenomLike(nom,prenom)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }  

    public List<AuteurResponseDto> getAuteursByNomStartWithIgnoreCase(String filter) {
        return auteurRepo.findByNomStartsWithIgnoreCase(filter)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }  

    public List<AuteurResponseDto> getByNomContainingIgnoreCase(String filter) {
       return auteurRepo.findByNomContainingIgnoreCase(filter)
               .stream()
               .map(mapper::toResponse)
               .toList();
    }

}
