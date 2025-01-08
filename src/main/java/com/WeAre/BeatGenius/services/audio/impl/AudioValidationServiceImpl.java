// src/main/java/com/WeAre/BeatGenius/services/audio/impl/AudioValidationServiceImpl.java
package com.WeAre.BeatGenius.services.audio.impl;

import com.WeAre.BeatGenius.domain.exceptions.InvalidFileException;
import com.WeAre.BeatGenius.services.audio.interfaces.AudioValidationService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class AudioValidationServiceImpl implements AudioValidationService {

  private static final Set<String> ALLOWED_MIME_TYPES = Set.of("audio/mpeg", "audio/wav");
  private static final int MIN_DURATION_SECONDS = 60; // 1 minute
  private static final int MAX_DURATION_SECONDS = 300; // 5 minutes
  private static final int MIN_BIT_RATE = 128; // 128 kbps minimum

  @Override
  public void validateAudioFile(MultipartFile file) throws InvalidFileException, IOException {
    validateMimeType(file);
    validateDuration(file);
    validateAudioQuality(file);
  }

  private void validateMimeType(MultipartFile file) throws InvalidFileException {
    String contentType = file.getContentType();
    if (!ALLOWED_MIME_TYPES.contains(contentType)) {
      log.error("Invalid file type: {}", contentType);
      throw new InvalidFileException("Le fichier doit être au format MP3 ou WAV");
    }
  }

  private void validateDuration(MultipartFile file) throws InvalidFileException, IOException {
    try (AudioInputStream audioInputStream =
        AudioSystem.getAudioInputStream(new ByteArrayInputStream(file.getBytes()))) {

      AudioFormat format = audioInputStream.getFormat();
      long frames = audioInputStream.getFrameLength();
      double durationInSeconds = frames / format.getFrameRate();

      if (durationInSeconds < MIN_DURATION_SECONDS || durationInSeconds > MAX_DURATION_SECONDS) {
        log.error("Invalid duration: {} seconds", durationInSeconds);
        throw new InvalidFileException("La durée doit être entre 1 et 5 minutes");
      }
    } catch (UnsupportedAudioFileException e) {
      log.error("Error reading audio file", e);
      throw new InvalidFileException("Format audio non supporté");
    }
  }

  private void validateAudioQuality(MultipartFile file) throws InvalidFileException, IOException {
    try (AudioInputStream audioInputStream =
        AudioSystem.getAudioInputStream(new ByteArrayInputStream(file.getBytes()))) {

      AudioFormat format = audioInputStream.getFormat();

      // Vérification du bitrate pour les MP3
      if ("audio/mpeg".equals(file.getContentType())) {
        float bitRate = calculateMp3BitRate(file.getBytes(), format);
        if (bitRate < MIN_BIT_RATE) {
          log.error("Insufficient bit rate: {} kbps", bitRate);
          throw new InvalidFileException(
              "La qualité audio est insuffisante. Minimum " + MIN_BIT_RATE + " kbps requis");
        }
      }

      // Pour les WAV, vérifier la fréquence d'échantillonnage
      if ("audio/wav".equals(file.getContentType()) && format.getSampleRate() < 44100) {
        log.error("Insufficient sample rate: {} Hz", format.getSampleRate());
        throw new InvalidFileException(
            "La fréquence d'échantillonnage doit être d'au moins 44.1 kHz");
      }
    } catch (UnsupportedAudioFileException e) {
      log.error("Error analyzing audio quality", e);
      throw new InvalidFileException("Impossible d'analyser la qualité audio");
    }
  }

  private float calculateMp3BitRate(byte[] fileData, AudioFormat format) {
    // Calcul approximatif du bitrate pour MP3
    float duration = fileData.length / format.getFrameRate();
    return (fileData.length * 8) / (duration * 1000); // kbps
  }
}
