package com.WeAre.BeatGenius.api.dto.requests.beat;

import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.Note;
import com.WeAre.BeatGenius.domain.enums.Scale;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "DTO pour la mise à jour d'un beat")
public class UpdateBeatRequest {
  @Schema(example = "Trap Melody")
  private String title;

  @Schema(example = "Un beat trap mélodique avec des sonorités orientales")
  private String description;

  @Schema(example = "TRAP")
  private Genre genre;

  @ArraySchema(
      schema = @Schema(example = "melodic"),
      arraySchema = @Schema(description = "Liste des tags"))
  private List<String> tags;

  @Schema(example = "140")
  private Integer bpm;

  @Schema(example = "C")
  private Note note;

  @Schema(example = "MINOR")
  private Scale scale;

  @ArraySchema(
      schema = @Schema(example = "dark"),
      arraySchema = @Schema(description = "Liste des ambiances"))
  private List<String> moods;

  @ArraySchema(
      schema = @Schema(example = "808"),
      arraySchema = @Schema(description = "Liste des instruments"))
  private List<String> instruments;

  @Schema(example = "2025-01-04T00:00:00Z")
  private LocalDateTime releaseDate;

  @Schema(example = "true")
  private Boolean includeForBulkDiscounts;
}
  // Je crois qu'il faut gérer ce cas de figure pour pouvoir modifier l'entierté du beat qu'on
  // update ( et pas seulement title description & genre)
  // et rajouter un updated_at a chaque fois qu'on modifie tel ou tel beat
