package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface GenreDocumentMapper {
    GenreDocumentResponseDto toResponse (GenreDocument genreDocument);
}
