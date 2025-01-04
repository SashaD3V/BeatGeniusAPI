package com.WeAre.BeatGenius.api.dto.requests.marketplace;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

// CreateLicenseRequest
@Data
@Builder  // Ajoutez cette annotation
public class CreateLicenseRequest {
  private String name;
  private LicenseType type;
  private BigDecimal price;
  private String fileFormat;
  private String rights;
  private Boolean isTagged;
  private String contractTerms;
  private Integer distributionLimit;
  private Long beatId;
}
