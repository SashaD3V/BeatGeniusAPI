package com.WeAre.BeatGenius.api.dto.requests.beat;

import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseOptionRequest;
import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.Note;
import com.WeAre.BeatGenius.domain.enums.Scale;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBeatRequest {
  @NotBlank(message = "Le titre est obligatoire")
  @Schema(description = "Titre de l'instrumentale", example = "Trap Melody")
  private String title;

  @NotBlank(message = "L'URL audio est obligatoire")
  @Schema(
      description = "Lien vers le fichier audio",
      example = "https://storage.beatgenius.com/beats/trap-melody.mp3")
  private String audioUrl;

  @Schema(
      description = "Description du beat",
      example = "Un beat trap mélodique avec des sonorités orientales")
  private String description;

  @Schema(description = "Tags descriptifs", example = "['melodic', 'dark', 'oriental']")
  private List<String> tags;

  @Schema(description = "Tempo en BPM", example = "140")
  private Integer bpm;

  @Schema(
      description = "Note fondamentale",
      example = "C",
      allowableValues = {
        "C", "C_SHARP", "D", "D_SHARP", "E", "F", "F_SHARP", "G", "G_SHARP", "A", "A_SHARP", "B"
      })
  private Note note;

  @Schema(
      description = "Gamme (mode)",
      example = "MINOR",
      allowableValues = {"MAJOR", "MINOR"})
  private Scale scale;

  @Schema(description = "Ambiances du beat", example = "['dark', 'emotional', 'aggressive']")
  private List<String> moods;

  @Schema(description = "Instruments utilisés", example = "['808', 'piano', 'flute']")
  private List<String> instruments;

  @Schema(description = "Date de sortie prévue", example = "2025-01-04T00:00:00Z")
  private LocalDateTime releaseDate;

  @Schema(description = "Éligible aux réductions en lot", example = "true")
  private Boolean includeForBulkDiscounts;

  @NotNull(message = "Le genre est obligatoire")
  @Schema(
      description = "Genre musical",
      example = "TRAP",
      allowableValues = {"TRAP", "DRILL", "RNB", "BOOM_BAP", "POP"})
  private Genre genre;

  @Schema(description = "Licences disponibles pour ce beat")
  @NotEmpty(message = "Au moins une licence doit être proposée")
  @Size(min = 1, message = "Vous devez proposer au moins 1 licence")
  private List<CreateLicenseOptionRequest> licenses;
}
