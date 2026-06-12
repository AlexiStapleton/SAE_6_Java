package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.EvenementCreateDto;
import com.usmb.but3.td4biblio.dto.EvenementDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EvenementResponseDto;
import com.usmb.but3.td4biblio.entity.Evenement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BibliothequeMapper.class, TypeEvenementMapper.class})
public interface EvenementMapper extends GenericMapper<Evenement, EvenementResponseDto, EvenementDetailResponseDto, EvenementCreateDto> {
    @Override
    @Mapping(target = "nomBibliotheque", source = "bibliotheque.nom")
    @Mapping(target = "nomTypeEvenement", source = "typeEvenement.nom")
    EvenementResponseDto toResponse(Evenement evenement);

    @Override
    EvenementDetailResponseDto toDetailResponse(Evenement evenement);

    @Override
    @Mapping(target = "bibliotheque", ignore = true )
    @Mapping(target = "typeEvenement", ignore = true)
    Evenement toEntity(EvenementCreateDto dto);
}
