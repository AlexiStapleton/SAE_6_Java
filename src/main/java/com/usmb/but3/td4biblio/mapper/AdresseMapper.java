package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.DTO.AdresseResponseDto;
import com.usmb.but3.td4biblio.DTO.AdresseCreateDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AdresseMapper extends GenericMapper<Adresse, AdresseResponseDto, AdresseResponseDto, AdresseCreateDto>{

    @Named("adresseToResponse")
    AdresseResponseDto toResponse(Adresse adresse);

    @Named("adresseToDetailResponse")
    AdresseResponseDto toDetailResponse(Adresse adresse);

    Adresse toEntity(AdresseCreateDto dto);
}
