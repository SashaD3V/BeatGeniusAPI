package com.WeAre.BeatGenius.api.dto.generic;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String message;
  private LocalDateTime timestamp;
}