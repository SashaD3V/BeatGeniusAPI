// PricingConstants.java
package com.WeAre.BeatGenius.domain.constants;

import java.math.BigDecimal;

/** Constantes liées aux prix et aux licences */
public final class PricingConstants {
  private PricingConstants() {}

  // Prix des licences
  public static final BigDecimal BASIC_LICENSE_PRICE = new BigDecimal("29.99");
  public static final BigDecimal PREMIUM_LICENSE_PRICE = new BigDecimal("99.99");
  public static final BigDecimal EXCLUSIVE_LICENSE_PRICE = new BigDecimal("999.99");

  // Limites de distribution
  public static final int BASIC_LICENSE_DISTRIBUTION_LIMIT = 10_000;
  public static final int PREMIUM_LICENSE_DISTRIBUTION_LIMIT = 100_000;

  // Formats de fichiers par licence
  public static final String BASIC_LICENSE_FORMAT = "MP3";
  public static final String PREMIUM_LICENSE_FORMAT = "WAV + MP3";
  public static final String EXCLUSIVE_LICENSE_FORMAT = "WAV + MP3 + Trackouts";
}
