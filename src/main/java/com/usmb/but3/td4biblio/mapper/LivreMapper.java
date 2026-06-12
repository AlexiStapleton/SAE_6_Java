package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.LivreCreateDto;
import com.usmb.but3.td4biblio.dto.LivreDetailResponseDto;
import com.usmb.but3.td4biblio.dto.LivreResponseDto;
import com.usmb.but3.td4biblio.entity.Livre;
import org.mapstruct.*;

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

    @Mapping(source = "auteur.id", target = "auteurId")
    @Mapping(source = "editeur.id", target = "editeurId")
    @Mapping(source = "bibliotheque.id", target = "bibliothequeId")
    @Mapping(source = "genre.id", target = "genreId")
    @Mapping(source = "codeRaison.id", target = "codeRaisonId")
    LivreCreateDto fromDetailToCreate(LivreDetailResponseDto dto);

    @Override
    @Mapping(target = "auteur", ignore = true)
    @Mapping(target = "editeur", ignore = true)
    @Mapping(target = "bibliotheque", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "codeRaison", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Livre toEntity (LivreCreateDto dto);

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
    void updateFromDto(LivreCreateDto dto, @MappingTarget Livre livre);
}
