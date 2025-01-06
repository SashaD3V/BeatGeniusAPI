package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.api.dto.generic.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(InvalidFileException.class)
  public ResponseEntity<ErrorResponse> handleInvalidFile(InvalidFileException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
    return createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
    ErrorResponse error = new ErrorResponse(status.value(), message, LocalDateTime.now());
    return new ResponseEntity<>(error, status);
  }
}
