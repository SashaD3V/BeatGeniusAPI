package com.WeAre.BeatGenius.services.marketplace.interfaces;

import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.enums.LicenseType;

public interface LicenseTemplateService {
    License createLicenseFromTemplate(LicenseType type, Beat beat);
}