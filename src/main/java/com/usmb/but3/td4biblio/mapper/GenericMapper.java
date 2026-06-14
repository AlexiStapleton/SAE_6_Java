package com.usmb.but3.td4biblio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper <T, RespDto, DetailDto, CreateDto>{
    RespDto toResponse(T entity);
    DetailDto toDetailResponse(T entity);
    T toEntity(CreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateDto dto, @MappingTarget T Entity);
}
