package com.WeAre.BeatGenius.api.dto.requests.marketplace;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateLicenseRequest {
    private LicenseType type;
    private BigDecimal price;
    private String rights;
    private Long beatId;
}

