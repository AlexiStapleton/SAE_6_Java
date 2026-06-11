package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EditeurCreateDto;
import com.usmb.but3.td4biblio.dto.EditeurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.EditeurMapper;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EditeurService
                extends AbstractGenericService<Editeur, Integer, EditeurResponseDto, EditeurDetailResponseDto, EditeurCreateDto>{
    public EditeurService(EditeurRepo repository, EditeurMapper mapper) {
        super(repository, mapper);
    }
    @Override
    public EditeurResponseDto update(Integer id, EditeurCreateDto dto) {
        Editeur editeur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id));


        return mapper.toResponse(editeur);
    }


}
