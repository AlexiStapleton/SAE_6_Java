package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.EditeurCreateDto;
import com.usmb.but3.td4biblio.dto.EditeurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.EditeurResponseDto;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AdresseMapper.class, DocumentMapper.class})
public interface EditeurMapper extends GenericMapper<Editeur, EditeurResponseDto, EditeurDetailResponseDto, EditeurCreateDto> {

    @Named("editeurToResponse")
    EditeurResponseDto toResponse(Editeur editeur);

    @Named("editeurToDetailResponse")
    EditeurDetailResponseDto toDetailResponse(Editeur editeur);

    Editeur toEntity(EditeurCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "adresse", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(EditeurCreateDto dto, @MappingTarget Editeur editeur);

}
