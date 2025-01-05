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
}
