package com.WeAre.BeatGenius.api.dto.responses.beat;

import com.WeAre.BeatGenius.api.dto.generic.BaseDTO;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.CreditStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeatCreditResponse extends BaseDTO {
  @Schema(description = "Informations sur le producteur")
  private UserResponse producer;

  @Schema(description = "Rôle dans la production", example = "Co-producer")
  private String role;

  @Schema(description = "Pourcentage des profits", example = "25.5")
  private Double profitShare;

  @Schema(description = "Pourcentage des droits d'édition", example = "25.5")
  private Double publishingShare;

  @Schema(description = "Statut de validation du crédit", example = "PENDING")
  private CreditStatus status;
}
