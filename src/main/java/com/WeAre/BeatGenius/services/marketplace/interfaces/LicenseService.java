package com.WeAre.BeatGenius.services.marketplace.interfaces;

import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseOptionRequest;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.services.generic.interfaces.BaseService;

public interface LicenseService
        extends BaseService<License, LicenseResponse, CreateLicenseRequest, UpdateLicenseRequest> {

  License createStandardLicense(CreateLicenseOptionRequest option, Beat beat);

  License createLicense(CreateLicenseRequest request);

  License updateLicense(Long id, UpdateLicenseRequest request);

  void deleteLicense(Long id);

  License getLicense(Long id);
}