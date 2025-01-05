package com.WeAre.BeatGenius.api.dto.responses.beat;

import com.WeAre.BeatGenius.api.dto.generic.BaseDTO;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.Note;
import com.WeAre.BeatGenius.domain.enums.Scale;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeatResponse extends BaseDTO {
  @Schema(example = "Trap Beat #1")
  private String title;

  @Schema(example = "https://storage.beatgenius.com/beats/trap-beat-1.mp3")
  private String audioUrl;

  @Schema(example = "TRAP")
  private Genre genre;

  @Schema(description = "Description du beat")
  private String description;

  @Schema(description = "Tags associés au beat")
  private List<String> tags;

  @Schema(description = "BPM (Beats Per Minute)", example = "140")
  private Integer bpm;

  @Schema(description = "Note fondamentale")
  private Note note;

  @Schema(description = "Gamme")
  private Scale scale;

  @Schema(description = "Ambiances du beat")
  private List<String> moods;

  @Schema(description = "Instruments utilisés")
  private List<String> instruments;

  @Schema(description = "Date de sortie prévue")
  private LocalDateTime releaseDate;

  @Schema(description = "Éligible aux réductions en lot")
  private Boolean includeForBulkDiscounts;

  @Schema(description = "Producteur principal")
  private UserResponse producer;

  @Schema(description = "Licences disponibles")
  private List<LicenseResponse> licenses;

  @Schema(description = "Crédits du beat")
  private List<BeatCreditResponse> beatCredits;
}
