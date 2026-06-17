package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.AuteurCreateDto;
import com.usmb.but3.td4biblio.dto.AuteurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.AuteurResponseDto;
import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class, TypeAuteur.class})
public interface AuteurMapper extends GenericMapper<Auteur, AuteurResponseDto, AuteurDetailResponseDto, AuteurCreateDto> {

    @Override
    @Named("auteurToResponse")
    AuteurResponseDto toResponse(Auteur auteur);

    @Override
    @Named("auteurToDetailResponse")
    AuteurDetailResponseDto toDetailResponse(Auteur entity);

    @Override
    @Mapping( target = "documents", ignore = true)
    @Mapping( target = "typesAuteur", ignore = true)
    Auteur toEntity(AuteurCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "typesAuteur", ignore = true) // géré manuellement
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(AuteurCreateDto dto, @MappingTarget Auteur auteur);

    // MapStruct sait mapper List<TypeAuteur> -> List<String> avec cette méthode
    default String typeAuteurToString(TypeAuteur typeAuteur) {
        return typeAuteur.getNom();
    }
}
