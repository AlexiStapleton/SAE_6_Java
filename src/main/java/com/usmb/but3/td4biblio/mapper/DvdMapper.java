package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.DvdCreateDto;
import com.usmb.but3.td4biblio.dto.DvdDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DvdResponseDto;
import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.entity.Dvd;
import com.usmb.but3.td4biblio.entity.Livre;
import org.mapstruct.*;

@Mapper( componentModel = "spring", uses = {AuteurMapper.class, EditeurMapper.class, BibliothequeMapper.class, GenreDocumentMapper.class, CodeRaisonMapper.class})
public interface DvdMapper extends GenericMapper<Dvd, DvdResponseDto, DvdDetailResponseDto, DvdCreateDto> {

    @Mapping( source = "auteur.nom", target = "nomAuteur" )
    @Mapping( source = "editeur.nom", target = "nomEditeur" )
    @Mapping( source = "bibliotheque.nom", target = "nomBibliotheque" )
    @Mapping( source = "genre.nom", target = "nomGenreDocument" )
    @Mapping( source = "codeRaison.nom", target = "nomRaison" )
    DvdResponseDto toResponse(Dvd dvd);

    @Mapping(source = "editeur", target = "editeur", qualifiedByName = "editeurToResponse")
    DvdDetailResponseDto toDetailResponse(Dvd dvd);

    @Override
    @Mapping(target = "auteur", ignore = true)
    @Mapping(target = "editeur", ignore = true)
    @Mapping(target = "bibliotheque", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "codeRaison", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Dvd toEntity (DvdCreateDto dto);


    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "auteur", ignore = true)
    @Mapping(target = "editeur", ignore = true)
    @Mapping(target = "bibliotheque", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "codeRaison", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(DvdCreateDto dto, @MappingTarget Dvd dvd);

}
