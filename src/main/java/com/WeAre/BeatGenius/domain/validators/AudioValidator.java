// AudioValidator.java
package com.WeAre.BeatGenius.domain.validators;

import com.WeAre.BeatGenius.domain.constants.AudioConstants;
import com.WeAre.BeatGenius.domain.exceptions.InvalidFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.springframework.web.multipart.MultipartFile;

public class AudioValidator {
  private AudioValidator() {} // Constructeur privé pour class utilitaire

  public static void validateAudioFile(MultipartFile file)
      throws IOException, UnsupportedAudioFileException {
    validateMimeType(file);
    validateDuration(file);
  }

  private static void validateMimeType(MultipartFile file) {
    String contentType = file.getContentType();
    if (!Arrays.asList(AudioConstants.SUPPORTED_FORMATS).contains(contentType)) {
      throw new InvalidFileException(AudioConstants.ERROR_INVALID_FORMAT);
    }
  }

  private static void validateDuration(MultipartFile file)
      throws IOException, UnsupportedAudioFileException {
    try (AudioInputStream audioInputStream =
        AudioSystem.getAudioInputStream(new ByteArrayInputStream(file.getBytes()))) {
      AudioFormat format = audioInputStream.getFormat();
      long frames = audioInputStream.getFrameLength();
      double durationInSeconds = (frames / format.getFrameRate());

      if (durationInSeconds < AudioConstants.MIN_DURATION_SECONDS
          || durationInSeconds > AudioConstants.MAX_DURATION_SECONDS) {
        throw new InvalidFileException(AudioConstants.ERROR_INVALID_DURATION);
      }
    }
  }
}
