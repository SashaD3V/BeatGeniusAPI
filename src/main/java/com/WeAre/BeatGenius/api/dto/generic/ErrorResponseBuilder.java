package com.WeAre.BeatGenius.api.dto.generic;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseBuilder {
  private int status = 400; // Par défaut BAD_REQUEST
  private String code;
  private String message;
  private List<String> details;
  private String path;
  private String technicalDetails;
  private LocalDateTime timestamp = LocalDateTime.now();

  public ErrorResponseBuilder withStatus(int status) {
    this.status = status;
    return this;
  }

  public ErrorResponseBuilder withErrorCode(String code) {
    this.code = code;
    return this;
  }

  public ErrorResponseBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public ErrorResponseBuilder withDetails(List<String> details) {
    this.details = details;
    return this;
  }

  public ErrorResponseBuilder withPath(String path) {
    this.path = path;
    return this;
  }

  public ErrorResponseBuilder withTechnicalDetails(String technicalDetails) {
    this.technicalDetails = technicalDetails;
    return this;
  }

  public ErrorResponse build() {
    return new ErrorResponse(status, code, message, details, path, technicalDetails, timestamp);
  }
}
