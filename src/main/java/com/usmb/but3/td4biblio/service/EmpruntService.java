package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EmpruntCreateDto;
import com.usmb.but3.td4biblio.dto.EmpruntDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EmpruntResponseDto;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EmpruntMapper;
import com.usmb.but3.td4biblio.mapper.UtilisateurRepo;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.repository.EmpruntRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class EmpruntService extends AbstractGenericService<Emprunt, Emprunt.EmpruntId, EmpruntResponseDto, EmpruntDetailResponseDto, EmpruntCreateDto> {

    private final UtilisateurRepo utilisateurRepository;
    private final DocumentRepo documentRepository;
    public EmpruntService(EmpruntRepo repository, EmpruntMapper mapper, UtilisateurRepo utilisateurRepository, DocumentRepo documentRepository){
        super(repository, mapper);
        this.utilisateurRepository = utilisateurRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public EmpruntDetailResponseDto update(Emprunt.EmpruntId id, EmpruntCreateDto dto){
        Emprunt emprunt = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Emprunt non trouvé : " + id));

        mapper.updateFromDto(dto , emprunt);
        emprunt.setDocument(documentRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + dto.getDocumentId())));
        emprunt.setUtilisateur(utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + dto.getUtilisateurId())));

        return mapper.toDetailResponse(repository.save(emprunt));
    }

    @Override
    public EmpruntDetailResponseDto create(EmpruntCreateDto dto) {
        Emprunt emprunt = mapper.toEntity(dto);

        emprunt.setDocument(documentRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + dto.getDocumentId())));
        emprunt.setUtilisateur(utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RessourceNotFoundException("Document non trouvé : " + dto.getUtilisateurId())));

        emprunt.setDateCreation(LocalDate.now());

        return mapper.toDetailResponse(repository.save(emprunt));

    }
}
