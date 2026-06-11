package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.TypeAuteurCreateDto;
import com.usmb.but3.td4biblio.dto.TypeAuteurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.TypeAuteurResponseDto;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AuteurMapper.class})
public interface TypeAuteurMapper extends GenericMapper<TypeAuteur, TypeAuteurResponseDto, TypeAuteurDetailResponseDto, TypeAuteurCreateDto> {

}
