package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.BibliothequeMapper;
import com.usmb.but3.td4biblio.repository.AdresseRepo;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BibliothequeService
        extends AbstractGenericService<Bibliotheque, Integer, BibliothequeResponseDto, BibliothequeDetailResponseDto, BibliothequeCreateDto> {

    private final AdresseRepo adresseRepo;
    public BibliothequeService (BibliothequeRepo repository, BibliothequeMapper mapper, AdresseRepo adresseRepo) {
        super(repository, mapper);
        this.adresseRepo = adresseRepo;
    }

    @Override
    public BibliothequeDetailResponseDto update(Integer id, BibliothequeCreateDto dto){
        Bibliotheque bibliotheque = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvé avec l'id : " + id));

        bibliotheque.setAdresse(adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvé : " + dto.getAdresseId())));
        bibliotheque.setNom(dto.getNom());

        return mapper.toDetailResponse(repository.save(bibliotheque));
    }

    @Override
    public BibliothequeDetailResponseDto create(BibliothequeCreateDto dto) {
        Bibliotheque bibliotheque = mapper.toEntity(dto);

        bibliotheque.setAdresse(adresseRepo.findById(dto.getAdresseId())
                .orElseThrow(() -> new RessourceNotFoundException("Adresse non trouvé : " + dto.getAdresseId())));

        return mapper.toDetailResponse(repository.save(bibliotheque));
    }


}
