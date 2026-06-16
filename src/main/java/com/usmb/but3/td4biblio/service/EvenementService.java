package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.dto.EvenementDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EvenementResponseDto;
import com.usmb.but3.td4biblio.entity.Evenement;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EvenementMapper;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;
import com.usmb.but3.td4biblio.repository.EvenementRepo;
import com.usmb.but3.td4biblio.repository.TypeEvenementRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EvenementService extends AbstractGenericService<Evenement, Integer, EvenementResponseDto, EvenementDetailResponseDto, EvenementCreateDto>{

    BibliothequeRepo bibliothequeRepo;
    TypeEvenementRepo typeEvenementRepo;
    public EvenementService(EvenementRepo repository, EvenementMapper mapper, BibliothequeRepo bibliothequeRepo, TypeEvenementRepo typeEvenementRepo) {
        super(repository, mapper);
        this.bibliothequeRepo = bibliothequeRepo;
        this.typeEvenementRepo = typeEvenementRepo;
    }

    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public EvenementDetailResponseDto update(Integer id, EvenementCreateDto dto){
        Evenement evenement = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Évenement non trouvé : " + id));

        mapper.updateFromDto(dto, evenement);

        evenement.setBibliotheque(bibliothequeRepo.findById(dto.getBibliothequeId())
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvée : " + dto.getBibliothequeId())));

        evenement.setTypeEvenement(typeEvenementRepo.findById(dto.getTypeEvenementId())
                .orElseThrow(() -> new RessourceNotFoundException("Type évènement non trouvé : " + dto.getTypeEvenementId())));

        return mapper.toDetailResponse(repository.save(evenement));

    }

    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public EvenementDetailResponseDto create(EvenementCreateDto dto) {
        Evenement evenement = mapper.toEntity(dto);

        evenement.setBibliotheque(bibliothequeRepo.findById(dto.getBibliothequeId())
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvée : " + dto.getBibliothequeId())));

        evenement.setTypeEvenement(typeEvenementRepo.findById(dto.getTypeEvenementId())
                .orElseThrow(() -> new RessourceNotFoundException("Type évènement non trouvé : " + dto.getTypeEvenementId())));

        return mapper.toDetailResponse(repository.save(evenement));
    }

    @Override
    @PreAuthorize("hasRole('BIBLIOTHECAIRE')")
    public void delete(Integer id) {
        super.delete(id);
    }

    public Page<EvenementResponseDto> findPaginated(String searchTerm, Pageable pageable) {
        return ((EvenementRepo) repository)
                .findByNomContainingIgnoreCase(searchTerm, pageable)
                .map(mapper::toResponse);
    }
}
