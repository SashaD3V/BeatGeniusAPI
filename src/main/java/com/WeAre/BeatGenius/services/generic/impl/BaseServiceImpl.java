package com.WeAre.BeatGenius.services.generic.impl;

import com.WeAre.BeatGenius.api.dto.generic.BaseDTO;
import com.WeAre.BeatGenius.domain.entities.BaseEntity;
import com.WeAre.BeatGenius.domain.exceptions.ResourceNotFoundException;
import com.WeAre.BeatGenius.domain.mappers.BaseMapper;
import com.WeAre.BeatGenius.services.generic.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// GenericServiceImpl.java
public abstract class BaseServiceImpl<E extends BaseEntity, D extends BaseDTO, C, U>
    implements BaseService<E, D, C, U> {
  protected final JpaRepository<E, Long> repository;
  protected final BaseMapper<E, D, C, U> mapper;

  protected BaseServiceImpl(JpaRepository<E, Long> repository, BaseMapper<E, D, C, U> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public D create(C createDto) {
    E entity = mapper.toEntity(createDto);
    E savedEntity = repository.save(entity);
    return mapper.toDto(savedEntity);
  }

  @Override
  public D update(Long id, U updateDto) {
    E entity =
        repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    mapper.updateEntityFromDto(updateDto, entity);
    E updatedEntity = repository.save(entity);
    return mapper.toDto(updatedEntity);
  }

  @Override
  public D getById(Long id) {
    E entity =
        repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    return mapper.toDto(entity);
  }

  @Override
  public Page<D> getAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDto);
  }

  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Entity not found");
    }
    repository.deleteById(id);
  }
}
