package com.WeAre.BeatGenius.domain.validators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.WeAre.BeatGenius.domain.constants.AudioConstants;
import com.WeAre.BeatGenius.domain.exceptions.InvalidFileException;
import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AudioValidatorTest {

  @Mock private MultipartFile mockFile;
  private static MockedStatic<AudioSystem> mockedAudioSystem;
  private AudioInputStream mockAudioStream;

  @BeforeAll
  static void setUp() {
    mockedAudioSystem = mockStatic(AudioSystem.class);
  }

  @AfterAll
  static void tearDown() {
    mockedAudioSystem.close();
  }

  @Nested
  @DisplayName("Tests de validation des fichiers audio")
  class AudioFileValidation {

    @Test
    @DisplayName("Devrait rejeter un fichier avec un format non supporté")
    void shouldRejectUnsupportedFormat() {
      when(mockFile.getContentType()).thenReturn("audio/aac");

      InvalidFileException exception =
          assertThrows(
              InvalidFileException.class, () -> AudioValidator.validateAudioFile(mockFile));

      assertEquals(AudioConstants.ERROR_INVALID_FORMAT, exception.getMessage());
    }

    @Test
    @DisplayName("Devrait accepter un fichier MP3 valide")
    void shouldAcceptValidMP3() throws Exception {
      // Setup
      mockAudioStream = mock(AudioInputStream.class);
      AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
      when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_MP3);
      when(mockFile.getBytes()).thenReturn(new byte[1024]);
      when(mockAudioStream.getFormat()).thenReturn(format);
      when(mockAudioStream.getFrameLength()).thenReturn(44100L * 120);
      mockedAudioSystem
          .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
          .thenReturn(mockAudioStream);

      assertDoesNotThrow(() -> AudioValidator.validateAudioFile(mockFile));
    }

    @Test
    @DisplayName("Devrait accepter un fichier WAV valide")
    void shouldAcceptValidWAV() throws Exception {
      // Setup
      mockAudioStream = mock(AudioInputStream.class);
      AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
      when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_WAV);
      when(mockFile.getBytes()).thenReturn(new byte[1024]);
      when(mockAudioStream.getFormat()).thenReturn(format);
      when(mockAudioStream.getFrameLength()).thenReturn(44100L * 120);
      mockedAudioSystem
          .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
          .thenReturn(mockAudioStream);

      assertDoesNotThrow(() -> AudioValidator.validateAudioFile(mockFile));
    }

    @Test
    @DisplayName("Devrait rejeter un fichier trop court")
    void shouldRejectTooShortFile() throws Exception {
      // Setup
      mockAudioStream = mock(AudioInputStream.class);
      AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
      when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_MP3);
      when(mockFile.getBytes()).thenReturn(new byte[1024]);
      when(mockAudioStream.getFormat()).thenReturn(format);
      when(mockAudioStream.getFrameLength()).thenReturn(44100L * 30);
      mockedAudioSystem
          .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
          .thenReturn(mockAudioStream);

      InvalidFileException exception =
          assertThrows(
              InvalidFileException.class, () -> AudioValidator.validateAudioFile(mockFile));

      assertEquals(AudioConstants.ERROR_INVALID_DURATION, exception.getMessage());
    }

    @Test
    @DisplayName("Devrait rejeter un fichier trop long")
    void shouldRejectTooLongFile() throws Exception {
      // Setup
      mockAudioStream = mock(AudioInputStream.class);
      AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
      when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_MP3);
      when(mockFile.getBytes()).thenReturn(new byte[1024]);
      when(mockAudioStream.getFormat()).thenReturn(format);
      when(mockAudioStream.getFrameLength()).thenReturn(44100L * 360);
      mockedAudioSystem
          .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
          .thenReturn(mockAudioStream);

      InvalidFileException exception =
          assertThrows(
              InvalidFileException.class, () -> AudioValidator.validateAudioFile(mockFile));

      assertEquals(AudioConstants.ERROR_INVALID_DURATION, exception.getMessage());
    }

    @Nested
    @DisplayName("Tests de validation de la qualité audio")
    class AudioQualityValidation {

      @Test
      @DisplayName("Devrait accepter un MP3 en haute qualité")
      void shouldAcceptHighQualityMP3() throws Exception {
        // Setup
        mockAudioStream = mock(AudioInputStream.class);
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_MP3);
        when(mockFile.getBytes()).thenReturn(new byte[44100 * 16 * 120]); // ~320 kbps
        when(mockAudioStream.getFormat()).thenReturn(format);
        when(mockAudioStream.getFrameLength()).thenReturn(44100L * 120);
        mockedAudioSystem
            .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
            .thenReturn(mockAudioStream);

        assertDoesNotThrow(() -> AudioValidator.validateAudioFile(mockFile));
      }

      @Test
      @DisplayName("Devrait rejeter un MP3 en basse qualité")
      void shouldRejectLowQualityMP3() throws Exception {
        // Setup
        mockAudioStream = mock(AudioInputStream.class);
        AudioFormat format = new AudioFormat(22050, 8, 1, true, true);
        when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_MP3);

        // Simuler un MP3 de 64 kbps
        // Taille = (64000 bits/sec * 120 sec) / 8 bits/byte = 960000 bytes
        int fileSize = (64 * 1000 * 120) / 8;
        when(mockFile.getBytes()).thenReturn(new byte[fileSize]);

        when(mockAudioStream.getFormat()).thenReturn(format);
        // Frames pour 120 secondes à 22050 Hz
        when(mockAudioStream.getFrameLength()).thenReturn(22050L * 120);
        mockedAudioSystem
            .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
            .thenReturn(mockAudioStream);

        InvalidFileException exception =
            assertThrows(
                InvalidFileException.class, () -> AudioValidator.validateAudioFile(mockFile));

        assertEquals(AudioConstants.ERROR_INVALID_QUALITY, exception.getMessage());
      }

      @Test
      @DisplayName("Devrait accepter un WAV en haute qualité")
      void shouldAcceptHighQualityWAV() throws Exception {
        // Setup
        mockAudioStream = mock(AudioInputStream.class);
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_WAV);
        when(mockFile.getBytes()).thenReturn(new byte[44100 * 4 * 120]);
        when(mockAudioStream.getFormat()).thenReturn(format);
        when(mockAudioStream.getFrameLength()).thenReturn(44100L * 120);
        mockedAudioSystem
            .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
            .thenReturn(mockAudioStream);

        assertDoesNotThrow(() -> AudioValidator.validateAudioFile(mockFile));
      }

      @Test
      @DisplayName("Devrait rejeter un WAV en basse qualité")
      void shouldRejectLowQualityWAV() throws Exception {
        // Setup
        mockAudioStream = mock(AudioInputStream.class);
        AudioFormat format = new AudioFormat(22050, 8, 1, true, true);
        when(mockFile.getContentType()).thenReturn(AudioConstants.MIME_TYPE_WAV);
        when(mockFile.getBytes()).thenReturn(new byte[22050 * 1 * 120]);
        when(mockAudioStream.getFormat()).thenReturn(format);
        when(mockAudioStream.getFrameLength()).thenReturn(22050L * 120);
        mockedAudioSystem
            .when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
            .thenReturn(mockAudioStream);

        InvalidFileException exception =
            assertThrows(
                InvalidFileException.class, () -> AudioValidator.validateAudioFile(mockFile));

        assertEquals(AudioConstants.ERROR_INVALID_QUALITY, exception.getMessage());
      }
    }
  }
}
