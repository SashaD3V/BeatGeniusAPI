package com.WeAre.BeatGenius.api.requests.marketplace;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateLicenseRequest {
    private LicenseType type;
    private BigDecimal price;
    private String rights;
}