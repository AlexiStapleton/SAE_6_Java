package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.TypeEvenementCreateDto;
import com.usmb.but3.td4biblio.dto.TypeEvenementDetailResponseDto;
import com.usmb.but3.td4biblio.dto.TypeEvenementResponseDto;
import com.usmb.but3.td4biblio.entity.TypeEvenement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EvenementMapper.class})
public interface TypeEvenementMapper extends GenericMapper<TypeEvenement, TypeEvenementResponseDto, TypeEvenementDetailResponseDto, TypeEvenementCreateDto> {
}
