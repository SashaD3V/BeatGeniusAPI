// LicenseConstants.java
package com.WeAre.BeatGenius.domain.constants;

/** Constantes liées aux termes des licences */
public final class LicenseConstants {
  private LicenseConstants() {}

  // Termes des contrats
  public static final String BASIC_LICENSE_TERMS =
      """
        - MP3 file only
        - Distribution up to 10,000 copies
        - Must credit producer
        - For non-profit use only
        - Audio is tagged with producer tag
        """;

  public static final String PREMIUM_LICENSE_TERMS =
      """
        - WAV + MP3 files
        - Distribution up to 100,000 copies
        - Must credit producer
        - Commercial use allowed
        - Untagged audio files
        """;

  public static final String EXCLUSIVE_LICENSE_TERMS =
      """
        - WAV + MP3 + Trackout files
        - Unlimited distribution
        - Full exclusive rights
        - Commercial use allowed
        - Beat will be removed from marketplace
        - Complete ownership transfer
        """;
}
