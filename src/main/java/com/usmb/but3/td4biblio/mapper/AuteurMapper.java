package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.AuteurCreateDto;
import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface AuteurMapper {
    @Mapping(target = "lesTypes", ignore = true)
    @Mapping(target = "lesDocuments", ignore = true)
    AuteurResponseDto toResponse(Auteur auteur);

    @Mapping( target = "documents", ignore = true)
    @Mapping( target = "typesAuteur", ignore = true)
    Auteur toEntity(AuteurCreateDto dto);

    // MapStruct sait mapper List<TypeAuteur> -> List<String> avec cette méthode
    default String typeAuteurToString(TypeAuteur typeAuteur) {
        return typeAuteur.getNom();
    }
}
