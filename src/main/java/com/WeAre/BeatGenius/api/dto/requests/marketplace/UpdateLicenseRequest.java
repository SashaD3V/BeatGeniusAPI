package com.WeAre.BeatGenius.api.dto.requests.marketplace;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateLicenseRequest {
  private LicenseType type;
  private BigDecimal price;
  private String rights;
}
