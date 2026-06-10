package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.mapper.EditeurMapper;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditeurService {
    private final EditeurRepo editeurRepo;
    private final EditeurMapper editeurMapper;

    public List<EditeurResponseDto> getAllEditeurs() {
        return editeurRepo.findAll()
                .stream()
                .map(editeurMapper::toResponse)
                .toList();
    }


}
