package com.WeAre.BeatGenius.services.generic.interfaces;

import com.WeAre.BeatGenius.api.dto.generic.BaseDTO;
import com.WeAre.BeatGenius.domain.entities.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<E extends BaseEntity, D extends BaseDTO, C, U> {
  D create(C createRequest);

  D update(Long id, U updateRequest);

  D getById(Long id);

  Page<D> getAll(Pageable pageable);

  void delete(Long id);
}
