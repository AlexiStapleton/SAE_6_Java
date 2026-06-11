package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.DvdCreateDto;
import com.usmb.but3.td4biblio.dto.DvdDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DvdResponseDto;
import com.usmb.but3.td4biblio.entity.Dvd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring", uses = {AuteurMapper.class, EditeurMapper.class, BibliothequeMapper.class, GenreDocumentMapper.class, CodeRaisonMapper.class})
public interface DvdMapper extends GenericMapper<Dvd, DvdResponseDto, DvdDetailResponseDto, DvdCreateDto> {

    @Mapping( source = "auteur.nom", target = "nomAuteur" )
    @Mapping( source = "editeur.nom", target = "nomEditeur" )
    @Mapping( source = "bibliotheque.nom", target = "nomBibliotheque" )
    @Mapping( source = "genre.nom", target = "nomGenreDocument" )
    @Mapping( source = "codeRaison.nom", target = "nomRaison" )
    DvdResponseDto toResponse(Dvd dvd);

    @Mapping(source = "editeur", target = "editeur", qualifiedByName = "editeurToResponse")
    @Mapping(source = "bibliotheque", target = "bibliotheque", qualifiedByName = "bibliothequeToResponse")
    DvdDetailResponseDto toDetailResponse(Dvd dvd);

    Dvd toEntity (DvdCreateDto dto);
}
