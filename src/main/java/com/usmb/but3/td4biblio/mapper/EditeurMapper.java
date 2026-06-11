package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.EditeurCreateDto;
import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {AdresseMapper.class})
public interface EditeurMapper extends GenericMapper<Editeur, EditeurResponseDto, EditeurResponseDto, EditeurCreateDto> {

    @Named("editeurToResponse")
    EditeurResponseDto toResponse(Editeur editeur);

    @Named("editeurToDetailResponse")
    EditeurResponseDto toDetailResponse(Editeur editeur);

    Editeur toEntity(EditeurCreateDto dto);

}
