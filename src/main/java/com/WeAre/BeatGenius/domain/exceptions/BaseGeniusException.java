package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.domain.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseGeniusException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String userMessage;
  private final String technicalDetails;
  private final HttpStatus httpStatus;

  protected BaseGeniusException(
      ErrorCode errorCode, String userMessage, String technicalDetails, Throwable cause) {
    super(userMessage, cause);
    this.errorCode = errorCode;
    this.userMessage = userMessage != null ? userMessage : errorCode.getDefaultMessage();
    this.technicalDetails = technicalDetails;
    this.httpStatus = errorCode.getHttpStatus();
  }

  protected BaseGeniusException(ErrorCode errorCode, String userMessage) {
    this(errorCode, userMessage, null, null);
  }

  protected BaseGeniusException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, null, cause);
  }

  protected BaseGeniusException(ErrorCode errorCode) {
    this(errorCode, null, null, null);
  }
}
