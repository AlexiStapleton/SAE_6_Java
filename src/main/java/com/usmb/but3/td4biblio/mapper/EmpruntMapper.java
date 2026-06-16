package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.EmpruntCreateDto;
import com.usmb.but3.td4biblio.dto.EmpruntDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EmpruntResponseDto;
import com.usmb.but3.td4biblio.entity.Emprunt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class, UtilisateurMapper.class})
public interface EmpruntMapper extends GenericMapper<Emprunt, EmpruntResponseDto, EmpruntDetailResponseDto, EmpruntCreateDto> {

    @Override
    @Mapping(target = "nomUtilisateur", source = "utilisateur.nom")
    @Mapping(target = "nomDocument", source = "document.titre")
    EmpruntResponseDto toResponse(Emprunt emprunt);

    @Override
    EmpruntDetailResponseDto toDetailResponse(Emprunt emprunt);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    Emprunt toEntity(EmpruntCreateDto dto);

}
