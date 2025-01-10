package com.WeAre.BeatGenius.domain.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // Erreurs d'authentification et autorisation
  UNAUTHORIZED("AUTH-001", "Non autorisé", HttpStatus.UNAUTHORIZED),
  INVALID_CREDENTIALS("AUTH-002", "Identifiants invalides", HttpStatus.UNAUTHORIZED),
  ACCESS_DENIED("AUTH-003", "Accès refusé", HttpStatus.FORBIDDEN),

  // Erreurs de validation
  INVALID_INPUT("VAL-001", "Données d'entrée invalides", HttpStatus.BAD_REQUEST),
  INVALID_FILE_FORMAT("VAL-002", "Format de fichier non supporté", HttpStatus.BAD_REQUEST),
  FILE_TOO_LARGE("VAL-003", "Fichier trop volumineux", HttpStatus.PAYLOAD_TOO_LARGE),

  // Erreurs métier audio
  AUDIO_PROCESSING_ERROR(
      "AUDIO-001", "Erreur lors du traitement audio", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_AUDIO_QUALITY("AUDIO-002", "Qualité audio insuffisante", HttpStatus.BAD_REQUEST),

  // Erreurs de paiement
  PAYMENT_FAILED("PAY-001", "Le paiement a échoué", HttpStatus.BAD_REQUEST),
  INVALID_PAYMENT_INFO("PAY-002", "Informations de paiement invalides", HttpStatus.BAD_REQUEST),

  // Erreurs de licence
  LICENSE_NOT_AVAILABLE("LIC-001", "Licence non disponible", HttpStatus.BAD_REQUEST),
  INVALID_LICENSE_TYPE("LIC-002", "Type de licence invalide", HttpStatus.BAD_REQUEST),

  // Erreurs système
  INTERNAL_ERROR("SYS-001", "Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR),
  DATABASE_ERROR("SYS-002", "Erreur de base de données", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final String defaultMessage;
  private final HttpStatus httpStatus;

  ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
    this.code = code;
    this.defaultMessage = defaultMessage;
    this.httpStatus = httpStatus;
  }
}
