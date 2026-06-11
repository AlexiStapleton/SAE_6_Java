package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.AdresseResponseDto;
import com.usmb.but3.td4biblio.entity.Adresse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdresseMapper {
    AdresseResponseDto toResponse(Adresse adresse);
}
