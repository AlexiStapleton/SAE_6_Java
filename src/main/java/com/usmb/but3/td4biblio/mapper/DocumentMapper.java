package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import org.mapstruct.*;

@Mapper (componentModel = "spring", uses = {AuteurMapper.class, EditeurMapper.class, BibliothequeMapper.class, GenreDocumentMapper.class, CodeRaisonMapper.class})
public interface DocumentMapper extends GenericMapper <Document, DocumentResponseDto, DocumentDetailResponseDto, DocumentCreateDto> {

    @Mapping( source = "auteur.nom", target = "nomAuteur" )
    @Mapping( source = "editeur.nom", target = "nomEditeur" )
    @Mapping( source = "bibliotheque.nom", target = "nomBibliotheque" )
    @Mapping( source = "genre.nom", target = "nomGenreDocument" )
    @Mapping( source = "codeRaison.nom", target = "nomRaison" )
    DocumentResponseDto toResponse(Document document);

    @Mapping(source = "editeur", target = "editeur", qualifiedByName = "editeurToResponse")
    DocumentDetailResponseDto toDetailResponse(Document document);

    @Mapping(target = "auteur", ignore = true)
    @Mapping(target = "editeur", ignore = true)
    @Mapping(target = "bibliotheque", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "codeRaison", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toEntity (DocumentCreateDto dto);

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
    void updateFromDto(DocumentCreateDto dto, @MappingTarget Document document);
}
