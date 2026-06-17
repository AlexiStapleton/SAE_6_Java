package com.usmb.but3.td4biblio.mapper;

import com.usmb.but3.td4biblio.dto.GenreDocumentCreateDto;
import com.usmb.but3.td4biblio.dto.GenreDocumentResponseDto;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper (componentModel = "spring")
public interface GenreDocumentMapper extends GenericMapper<GenreDocument, GenreDocumentResponseDto, GenreDocumentResponseDto, GenreDocumentCreateDto> {
    @Named("genreToResponse")
    GenreDocumentResponseDto toResponse (GenreDocument genreDocument);

    @Named("genreToDetailResponse")
    GenreDocumentResponseDto toDetailResponseDto(GenreDocument genreDocument);

    GenreDocument toEntity(GenreDocumentCreateDto dto);
}
