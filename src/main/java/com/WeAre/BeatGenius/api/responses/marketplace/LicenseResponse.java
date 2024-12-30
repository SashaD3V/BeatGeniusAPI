package com.WeAre.BeatGenius.api.responses.marketplace;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LicenseResponse {
    private Long id;
    private LicenseType type;
    private BigDecimal price;
    private String rights;
    private Long beatId;
    // Eventuellement des infos basiques sur le beat associé
}