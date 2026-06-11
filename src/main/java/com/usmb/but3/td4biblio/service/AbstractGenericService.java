package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.GenericMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractGenericService<T, ID, RespDto, DetailDto, CreateDto>
        implements GenericService<T, ID, RespDto, DetailDto ,CreateDto> {

    protected final JpaRepository<T, ID> repository;
    protected final GenericMapper<T, RespDto, DetailDto, CreateDto> mapper;

    @Override
    public List<RespDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public DetailDto getById(ID id) {
        return mapper.toDetailResponse(
                repository.findById(id).orElseThrow(() -> new RessourceNotFoundException("Entité non trouvée : " + id))
        );
    }

    @Override
    public RespDto create(CreateDto dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new RessourceNotFoundException("Entité non trouvée : " + id);
        }
        repository.deleteById(id);
    }

    public abstract RespDto update(ID id, CreateDto dto);


}

