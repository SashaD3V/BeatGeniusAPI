package com.WeAre.BeatGenius.api.dto.generic;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String code;
  private String message;
  private List<String> details;
  private String path;
  private String technicalDetails;
  private LocalDateTime timestamp;
}
