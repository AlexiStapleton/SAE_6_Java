package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BibliothequeMapper {
    BibliothequeResponseDto toResponseDto (Bibliotheque bibliotheque);
}
