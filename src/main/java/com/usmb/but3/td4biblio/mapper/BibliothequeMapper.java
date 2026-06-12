package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeDetailResponseDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {EvenementMapper.class, DocumentMapper.class})
public interface BibliothequeMapper extends GenericMapper<Bibliotheque, BibliothequeResponseDto, BibliothequeDetailResponseDto, BibliothequeCreateDto> {

    @Override
    @Mapping(target = "adresse", ignore = true)
    Bibliotheque toEntity (BibliothequeCreateDto dto);
}
