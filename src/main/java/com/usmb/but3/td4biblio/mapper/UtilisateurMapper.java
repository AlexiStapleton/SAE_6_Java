package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.UtilisateurDetailResponseDto;
import com.usmb.but3.td4biblio.DTO.RegisterRequest;
import com.usmb.but3.td4biblio.dto.UtilisateurResponseDto;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AdresseMapper.class, EmpruntMapper.class})
public interface UtilisateurMapper extends GenericMapper<Utilisateur, UtilisateurResponseDto, UtilisateurDetailResponseDto, RegisterRequest> {

}
