package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.BibliothequeCreateDto;
import com.usmb.but3.td4biblio.dto.BibliothequeResponseDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BibliothequeMapper extends GenericMapper<Bibliotheque, BibliothequeResponseDto, BibliothequeResponseDto, BibliothequeCreateDto> {
    @Named("bibliothequeToResponse")
    BibliothequeResponseDto toResponseDto (Bibliotheque bibliotheque);
    @Named("bibliothequeToDetailResponse")
    BibliothequeResponseDto toDetailResponseDto (Bibliotheque bibliotheque);

    Bibliotheque toEntity(BibliothequeCreateDto dto);
}
