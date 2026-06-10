package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.CodeRaisonResponseDto;
import com.usmb.but3.td4biblio.entity.CodeRaison;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CodeRaisonMapper {
    CodeRaisonResponseDto toResponse (CodeRaison codeRaison);
}
