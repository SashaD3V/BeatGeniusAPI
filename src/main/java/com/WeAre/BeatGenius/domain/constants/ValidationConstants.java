package com.WeAre.BeatGenius.domain.constants;

import java.util.regex.Pattern;

public final class ValidationConstants {
  private ValidationConstants() {} // Empêche l'instanciation

  public static final int MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB
  public static final String[] ALLOWED_AUDIO_FORMATS = {"audio/mpeg", "audio/wav"};
  public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
  public static final int MAX_USERNAME_LENGTH = 50;
  public static final int MIN_PASSWORD_LENGTH = 8;
  public static final long MAX_BEAT_DURATION_SECONDS = 600; // 10 minutes
}
