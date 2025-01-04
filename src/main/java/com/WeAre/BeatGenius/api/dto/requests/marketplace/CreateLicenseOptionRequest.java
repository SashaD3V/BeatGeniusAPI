package com.WeAre.BeatGenius.api.dto.requests.marketplace;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateLicenseOptionRequest {
  @NotNull(message = "Le type de licence est requis")
  private LicenseType type;

  @NotNull(message = "Le prix est requis")
  @Min(value = 1, message = "Le prix doit être supérieur à 0")
  private BigDecimal price;
}
