package com.WeAre.BeatGenius.services.audio.interfaces;

import com.WeAre.BeatGenius.domain.exceptions.InvalidFileException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface AudioValidationService {
  /**
   * Valide un fichier audio selon les critères suivants : - Format : MP3 ou WAV - Durée : entre 1
   * et 5 minutes - Qualité : bitrate minimum de 128 kbps pour MP3, 44.1 kHz pour WAV
   *
   * @param file Le fichier audio à valider
   * @throws InvalidFileException si le fichier ne respecte pas les critères
   * @throws IOException en cas d'erreur lors de la lecture du fichier
   */
  void validateAudioFile(MultipartFile file) throws InvalidFileException, IOException;
}
