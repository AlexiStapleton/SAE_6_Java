package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.DTO.AuteurCreateDto;
import com.usmb.but3.td4biblio.DTO.AuteurResponseDto;
import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface AuteurMapper extends GenericMapper<Auteur, AuteurResponseDto, AuteurResponseDto, AuteurCreateDto> {

    @Named("auteurToResponse")
    AuteurResponseDto toResponse(Auteur auteur);

    @Named("auteurToDetailResponse")
    AuteurResponseDto toDetailResponse(Auteur entity);

    @Mapping( target = "documents", ignore = true)
    @Mapping( target = "typesAuteur", ignore = true)
    Auteur toEntity(AuteurCreateDto dto);

    // MapStruct sait mapper List<TypeAuteur> -> List<String> avec cette méthode
    default String typeAuteurToString(TypeAuteur typeAuteur) {
        return typeAuteur.getNom();
    }
}
