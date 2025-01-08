// AudioConstants.java
package com.WeAre.BeatGenius.domain.constants;

/** Constantes liées à la gestion des fichiers audio */
public final class AudioConstants {
  private AudioConstants() {} // Empêche l'instanciation

  // Durées en secondes
  public static final int MIN_DURATION_SECONDS = 60; // 1 minute
  public static final int MAX_DURATION_SECONDS = 300; // 5 minutes

  // Formats de fichiers
  public static final String MIME_TYPE_MP3 = "audio/mpeg";
  public static final String MIME_TYPE_WAV = "audio/wav";
  public static final String[] SUPPORTED_FORMATS = {MIME_TYPE_MP3, MIME_TYPE_WAV};

  // Messages d'erreur
  public static final String ERROR_INVALID_FORMAT = "Le fichier doit être au format MP3 ou WAV";
  public static final String ERROR_INVALID_DURATION = "La durée doit être entre 1 et 5 minutes";

  // Dans AudioConstants.java ajouter :
  public static final int MIN_BIT_RATE_KBPS = 128; // 128 kbps minimum pour MP3
  public static final float MIN_SAMPLE_RATE_HZ = 44100; // 44.1 kHz minimum
  public static final String ERROR_INVALID_QUALITY =
      "La qualité audio est insuffisante. Le fichier doit être d'au moins 128 kbps pour MP3 ou 44.1 kHz pour WAV";
}
