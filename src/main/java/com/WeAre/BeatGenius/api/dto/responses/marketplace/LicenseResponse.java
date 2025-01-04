package com.WeAre.BeatGenius.api.dto.responses.marketplace;

import com.WeAre.BeatGenius.api.dto.BaseDTO;
import com.WeAre.BeatGenius.domain.enums.LicenseType;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LicenseResponse extends BaseDTO {
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
