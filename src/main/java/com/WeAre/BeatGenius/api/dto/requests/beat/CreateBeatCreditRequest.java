package com.WeAre.BeatGenius.api.dto.requests.beat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBeatCreditRequest {
  @NotNull(message = "L'ID du producteur est requis")
  @Schema(description = "ID du producteur crédité", example = "1")
  private Long producerId;

  @Schema(description = "Rôle dans la production", example = "Co-producer")
  private String role;

  @NotNull(message = "Le pourcentage des profits est requis")
  @Min(value = 0, message = "Le pourcentage des profits doit être entre 0 et 100")
  @Max(value = 100, message = "Le pourcentage des profits doit être entre 0 et 100")
  @Schema(description = "Pourcentage des profits", example = "25.5")
  private Double profitShare;

  @NotNull(message = "Le pourcentage des droits d'édition est requis")
  @Min(value = 0, message = "Le pourcentage des droits d'édition doit être entre 0 et 100")
  @Max(value = 100, message = "Le pourcentage des droits d'édition doit être entre 0 et 100")
  @Schema(description = "Pourcentage des droits d'édition", example = "25.5")
  private Double publishingShare;
}
