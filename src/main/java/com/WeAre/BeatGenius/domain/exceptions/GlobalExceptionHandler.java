package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.api.dto.generic.ErrorResponse;
import com.WeAre.BeatGenius.api.dto.generic.ErrorResponseBuilder;
import com.WeAre.BeatGenius.domain.enums.ErrorCode;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private final ErrorResponseBuilder errorResponseBuilder;
  private final Environment environment;
  private final MessageSource messageSource;

  public GlobalExceptionHandler(
      ErrorResponseBuilder errorResponseBuilder,
      Environment environment,
      MessageSource messageSource) {
    this.errorResponseBuilder = errorResponseBuilder;
    this.environment = environment;
    this.messageSource = messageSource;
  }

  @ExceptionHandler(BaseGeniusException.class)
  public ResponseEntity<ErrorResponse> handleBeatGeniusException(
      BaseGeniusException ex, WebRequest request, Locale locale) {
    log.error("Domain error occurred: {}", ex.getUserMessage(), ex);

    String localizedMessage =
        messageSource.getMessage(
            "error." + ex.getErrorCode().getCode(), null, ex.getUserMessage(), locale);

    ErrorResponse error =
        errorResponseBuilder
            .withStatus(ex.getHttpStatus().value())
            .withErrorCode(ex.getErrorCode().getCode())
            .withMessage(localizedMessage)
            .withPath(request.getDescription(false))
            .build();

    if (environment.matchesProfiles("dev")) {
      error.setTechnicalDetails(ex.getTechnicalDetails());
    }

    return new ResponseEntity<>(error, ex.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, WebRequest request, Locale locale) {
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

    String message = messageSource.getMessage("error.validation.failed", null, locale);

    ErrorResponse response =
        errorResponseBuilder
            .withStatus(HttpStatus.BAD_REQUEST.value())
            .withErrorCode(ErrorCode.INVALID_INPUT.getCode())
            .withMessage(message)
            .withDetails(errors)
            .withPath(request.getDescription(false))
            .build();

    log.warn("Validation error: {}", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request, Locale locale) {
    String message = messageSource.getMessage("error.access.denied", null, locale);

    ErrorResponse response =
        errorResponseBuilder
            .withStatus(HttpStatus.FORBIDDEN.value())
            .withErrorCode(ErrorCode.ACCESS_DENIED.getCode())
            .withMessage(message)
            .withPath(request.getDescription(false))
            .build();

    log.warn("Access denied: {}", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex, WebRequest request, Locale locale) {
    String message = messageSource.getMessage("error.file.too.large", null, locale);

    ErrorResponse response =
        errorResponseBuilder
            .withStatus(HttpStatus.PAYLOAD_TOO_LARGE.value())
            .withErrorCode(ErrorCode.FILE_TOO_LARGE.getCode())
            .withMessage(message)
            .withPath(request.getDescription(false))
            .build();

    log.warn("File too large: {}", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDataAccessException(
      DataAccessException ex, WebRequest request, Locale locale) {
    String message = messageSource.getMessage("error.database", null, locale);

    ErrorResponse response =
        errorResponseBuilder
            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .withErrorCode(ErrorCode.DATABASE_ERROR.getCode())
            .withMessage(message)
            .withPath(request.getDescription(false))
            .build();

    log.error("Database error: ", ex);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException ex, WebRequest request, Locale locale) {
    String message = messageSource.getMessage("error.invalid.credentials", null, locale);

    ErrorResponse response =
        errorResponseBuilder
            .withStatus(HttpStatus.UNAUTHORIZED.value())
            .withErrorCode(ErrorCode.INVALID_CREDENTIALS.getCode())
            .withMessage(message)
            .withPath(request.getDescription(false))
            .build();

    log.warn("Invalid credentials attempt");
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, WebRequest request, Locale locale) {
    String message = messageSource.getMessage("error.internal", null, locale);

    ErrorResponse response =
        errorResponseBuilder
            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .withErrorCode(ErrorCode.INTERNAL_ERROR.getCode())
            .withMessage(message)
            .withPath(request.getDescription(false))
            .build();

    log.error("Unexpected error: ", ex);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
