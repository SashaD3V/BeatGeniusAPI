package com.WeAre.BeatGenius.api.dto.generic;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class BaseDTO {
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long version;
}
