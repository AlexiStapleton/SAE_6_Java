package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.entity.Livre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring", uses = {AuteurMapper.class, EditeurMapper.class, BibliothequeMapper.class, GenreDocumentMapper.class, CodeRaisonMapper.class})
public interface LivreMapper extends GenericMapper <Livre, LivreResponseDto, LivreDetailResponseDto, LivreCreateDto>{
    @Mapping( source = "auteur.nom", target = "nomAuteur" )
    @Mapping( source = "editeur.nom", target = "nomEditeur" )
    @Mapping( source = "bibliotheque.nom", target = "nomBibliotheque" )
    @Mapping( source = "genre.nom", target = "nomGenreDocument" )
    @Mapping( source = "codeRaison.nom", target = "nomRaison" )
    LivreResponseDto toResponse(Livre livre);

    @Mapping(source = "editeur", target = "editeur", qualifiedByName = "editeurToResponse")
    @Mapping(source = "auteur", target = "auteur", qualifiedByName = "auteurToResponse")
    LivreDetailResponseDto toDetailResponse(Livre livre);

    Livre toEntity (LivreCreateDto dto);
}
