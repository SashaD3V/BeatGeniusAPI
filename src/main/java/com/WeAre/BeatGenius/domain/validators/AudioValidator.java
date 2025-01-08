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
    validateAudioQuality(file); // Ajout de l'appel
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

  private static void validateAudioQuality(MultipartFile file)
      throws IOException, UnsupportedAudioFileException {
    try (AudioInputStream audioInputStream =
        AudioSystem.getAudioInputStream(new ByteArrayInputStream(file.getBytes()))) {

      AudioFormat format = audioInputStream.getFormat();

      if (AudioConstants.MIME_TYPE_MP3.equals(file.getContentType())) {
        // Pour MP3, vérifier le bitrate
        float bitRate = calculateMp3BitRate(file.getBytes(), format);
        if (bitRate < AudioConstants.MIN_BIT_RATE_KBPS) {
          throw new InvalidFileException(AudioConstants.ERROR_INVALID_QUALITY);
        }
      } else if (AudioConstants.MIME_TYPE_WAV.equals(file.getContentType())) {
        // Pour WAV, vérifier la fréquence d'échantillonnage
        if (format.getSampleRate() < AudioConstants.MIN_SAMPLE_RATE_HZ) {
          throw new InvalidFileException(AudioConstants.ERROR_INVALID_QUALITY);
        }
      }
    }
  }

  private static float calculateMp3BitRate(byte[] fileData, AudioFormat format) {
    try (AudioInputStream audioInputStream =
                 AudioSystem.getAudioInputStream(new ByteArrayInputStream(fileData))) {
      // On obtient la durée en secondes
      float durationInSeconds = audioInputStream.getFrameLength() / format.getFrameRate();

      // Pour un MP3 stéréo
      if (format.getChannels() == 2 && format.getSampleRate() >= 44100) {
        // Si c'est du stéréo haute qualité, on considère que c'est au moins 128 kbps
        return 192.0f;
      } else {
        // Sinon on considère que c'est de la basse qualité
        return 64.0f;
      }
    } catch (UnsupportedAudioFileException | IOException e) {
      throw new InvalidFileException(AudioConstants.ERROR_INVALID_QUALITY);
    }
  }
  }
