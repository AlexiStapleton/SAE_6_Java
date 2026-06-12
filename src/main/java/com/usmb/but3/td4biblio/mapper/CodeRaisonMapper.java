package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.DTO.CodeRaisonCreateDto;
import com.usmb.but3.td4biblio.DTO.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.entity.CodeRaison;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CodeRaisonMapper
extends GenericMapper<CodeRaison, CodeRaisonResponseDto, CodeRaisonResponseDto, CodeRaisonCreateDto> {
    @Named("codeRaisonToResponse")
    CodeRaisonResponseDto toResponse (CodeRaison codeRaison);

    @Named("codeRaisonToDetailResponse")
    CodeRaisonResponseDto toDetailResponse (CodeRaison codeRaison);

    CodeRaison toEntity (CodeRaisonCreateDto dto);

}
