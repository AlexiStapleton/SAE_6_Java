package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.GenreDocumentCreateDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.GenreDocumentMapper;
import com.usmb.but3.td4biblio.repository.GenreDocumentRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GenreDocumentService
    extends AbstractGenericService<GenreDocument, Integer, GenreDocumentResponseDto, GenreDocumentResponseDto, GenreDocumentCreateDto>{

    public GenreDocumentService(GenreDocumentRepo repository, GenreDocumentMapper mapper) {
        super(repository, mapper);
    }

    public GenreDocumentResponseDto update(Integer id, GenreDocumentCreateDto dto) {
        GenreDocument genreDocument = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Genre non trouvé avec id : " + id));

        return mapper.toResponse(genreDocument);
    }

}
