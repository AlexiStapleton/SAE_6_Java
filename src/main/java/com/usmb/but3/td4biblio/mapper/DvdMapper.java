package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.DvdCreateDto;
import com.usmb.but3.td4biblio.dto.DvdDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DvdResponseDto;
import com.usmb.but3.td4biblio.entity.Dvd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring", uses = {DocumentMapper.class, AuteurMapper.class, EditeurMapper.class})
public interface DvdMapper {

    @Mapping( source = "auteur.nom", target = "nomAuteur" )
    @Mapping( source = "editeur.nom", target = "nomEditeur" )
    @Mapping( source = "bibliotheque.nom", target = "nomBibliotheque" )
    @Mapping( source = "genre.nom", target = "nomGenreDocument" )
    @Mapping( source = "codeRaison.nom", target = "nomRaison" )
    DvdResponseDto toResponse(Dvd dvd);

    DvdDetailResponseDto toDetailResponse(Dvd dvd);

//    @Mapping(source = "auteurId", target = "auteur.id")
//    @Mapping(source = "editeurId", target = "editeur.id")
//    @Mapping(source = "bibliothequeId", target = "bibliotheque.id")
//    @Mapping(source = "genreDocumentId", target = "genre.id")
//    @Mapping(source = "codeRaisonId", target = "codeRaison.id")
    Dvd toEntity (DvdCreateDto dto);
}
