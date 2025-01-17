package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.generic.BaseDTO;
import com.WeAre.BeatGenius.domain.entities.generic.BaseEntity;
import org.mapstruct.MappingTarget;

public interface BaseMapper<E extends BaseEntity, D extends BaseDTO, C, U> {
  E toEntity(C createDto);

  D toDto(E entity);

  void updateEntityFromDto(U updateDto, @MappingTarget E entity);
}
