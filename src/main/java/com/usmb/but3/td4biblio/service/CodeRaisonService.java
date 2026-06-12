package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.CodeRaisonCreateDto;
import com.usmb.but3.td4biblio.dto.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.entity.CodeRaison;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.CodeRaisonMapper;
import com.usmb.but3.td4biblio.repository.CodeRaisonRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CodeRaisonService
    extends AbstractGenericService<CodeRaison, Integer, CodeRaisonResponseDto, CodeRaisonResponseDto, CodeRaisonCreateDto> {

    public CodeRaisonService(CodeRaisonRepo repository, CodeRaisonMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public CodeRaisonResponseDto update(Integer id, CodeRaisonCreateDto dto) {
        CodeRaison codeRaison = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Pas de code Raison avec l'id" + id));

        mapper.updateFromDto(dto, codeRaison);

        return mapper.toDetailResponse(repository.save(codeRaison));
    }
}
