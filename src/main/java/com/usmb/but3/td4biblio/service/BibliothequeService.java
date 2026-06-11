package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.BibliothequeMapper;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BibliothequeService
        extends AbstractGenericService<Bibliotheque, Integer, BibliothequeResponseDto, BibliothequeDetailResponseDto, BibliothequeCreateDto> {

    public BibliothequeService (BibliothequeRepo repository, BibliothequeMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public BibliothequeResponseDto update (Integer id, BibliothequeCreateDto dto){
        Bibliotheque bibliotheque = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Bibliothèque non trouvé avec l'id : " + id));

        return mapper.toResponse(bibliotheque);

    }
}
