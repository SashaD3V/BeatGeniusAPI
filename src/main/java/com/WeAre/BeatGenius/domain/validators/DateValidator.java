// DateValidator.java
package com.WeAre.BeatGenius.domain.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateValidator {
  private DateValidator() {} // Constructeur privé pour class utilitaire

  public static LocalDateTime validateAndConvertReleaseDate(String releaseDateStr) {
    if (releaseDateStr == null || releaseDateStr.isEmpty()) {
      return null;
    }

    try {
      return LocalDate.parse(releaseDateStr).atStartOfDay();
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Format de date invalide. Utilisez le format: YYYY-MM-DD");
    }
  }
}
