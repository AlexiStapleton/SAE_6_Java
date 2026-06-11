package com.usmb.but3.td4biblio.service;

import java.util.List;

public interface GenericService <T, ID, RespDto, DetailDto, CreateDto> {
    List<RespDto> getAll();
    DetailDto getById(ID id);
    RespDto create(CreateDto dto);
    RespDto update(ID id, CreateDto dto);
    void delete(ID id);
}
