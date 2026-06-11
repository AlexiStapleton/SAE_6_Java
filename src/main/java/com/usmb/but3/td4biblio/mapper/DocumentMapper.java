package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.DocumentCreateDto;
import com.usmb.but3.td4biblio.dto.DocumentDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DocumentResponseDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring", uses = {AuteurMapper.class, EditeurMapper.class, BibliothequeMapper.class, GenreDocumentMapper.class, CodeRaisonMapper.class})
public interface DocumentMapper {

    @Mapping( source = "auteur.nom", target = "nomAuteur" )
    @Mapping( source = "editeur.nom", target = "nomEditeur" )
    @Mapping( source = "bibliotheque.nom", target = "nomBibliotheque" )
    @Mapping( source = "genre.nom", target = "nomGenreDocument" )
    @Mapping( source = "codeRaison.nom", target = "nomRaison" )
    DocumentResponseDto toResponse(Document document);

    DocumentDetailResponseDto toDetailResponse(Document document);

    Document toEntity (DocumentCreateDto dto);


}
