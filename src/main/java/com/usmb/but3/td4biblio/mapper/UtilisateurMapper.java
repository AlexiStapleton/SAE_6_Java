package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.UtilisateurDetailResponseDto;
import com.usmb.but3.td4biblio.DTO.RegisterRequest;
import com.usmb.but3.td4biblio.dto.UtilisateurResponseDto;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AdresseMapper.class, EmpruntMapper.class})
public interface UtilisateurMapper extends GenericMapper<Utilisateur, UtilisateurResponseDto, UtilisateurDetailResponseDto, RegisterRequest> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adresse", ignore = true)
    @Mapping(target = "emprunts", ignore = true)
    @Mapping(target = "hashMotDePasse", ignore = true)
    @Mapping(target = "dateFinAbonnement", ignore = true)
    Utilisateur toEntity(RegisterRequest dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adresse", ignore = true)
    @Mapping(target = "emprunts", ignore = true)
    @Mapping(target = "hashMotDePasse", ignore = true)
    @Mapping(target = "dateFinAbonnement", ignore = true)
    void updateFromDto(RegisterRequest dto, @MappingTarget Utilisateur entity);
}
