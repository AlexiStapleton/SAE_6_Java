package com.usmb.but3.td4biblio.mapper;

public interface GenericMapper <T, RespDto, DetailDto, CreateDto>{
    RespDto toResponse(T entity);
    DetailDto toDetailResponse(T entity);
    T toEntity(CreateDto dto);
}
