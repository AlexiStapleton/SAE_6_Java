package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditeurMapper {
    @Mapping(target = "documents", ignore = true)
    EditeurResponseDto toResponse(Editeur editeur);

}
