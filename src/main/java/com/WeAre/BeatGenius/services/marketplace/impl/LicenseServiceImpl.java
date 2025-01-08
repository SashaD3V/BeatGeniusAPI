package com.WeAre.BeatGenius.services.marketplace.impl;

import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseOptionRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.LicenseResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.mappers.LicenseMapper;
import com.WeAre.BeatGenius.domain.repositories.LicenseRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatRepository;
import com.WeAre.BeatGenius.services.generic.impl.BaseServiceImpl;
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseService;
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseTemplateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl extends BaseServiceImpl<License, LicenseResponse, CreateLicenseRequest, UpdateLicenseRequest>
        implements LicenseService {

  private final LicenseMapper licenseMapper;
  private final BeatRepository beatRepository;
  private final LicenseRepository licenseRepository;
  private final LicenseTemplateService licenseTemplateService;

  public LicenseServiceImpl(
          LicenseRepository licenseRepository,
          LicenseMapper licenseMapper,
          BeatRepository beatRepository,
          LicenseTemplateService licenseTemplateService) {
    super(licenseRepository, licenseMapper);
    this.licenseMapper = licenseMapper;
    this.beatRepository = beatRepository;
    this.licenseRepository = licenseRepository;
    this.licenseTemplateService = licenseTemplateService;
  }

  @Override
  @Transactional
  public License createStandardLicense(CreateLicenseOptionRequest option, Beat beat) {
    License license = licenseTemplateService.createLicenseFromTemplate(option.getType(), beat);
    return licenseRepository.save(license);
  }

  @Override
  @Transactional
  public License createLicense(CreateLicenseRequest request) {
    License license = licenseMapper.toEntity(request);
    license.setBeat(
            beatRepository
                    .findById(request.getBeatId())
                    .orElseThrow(() -> new EntityNotFoundException("Beat not found")));
    return licenseRepository.save(license);
  }

  @Override
  @Transactional
  public License updateLicense(Long id, UpdateLicenseRequest request) {
    License license =
            licenseRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("License not found"));

    if (request.getType() != null) {
      license.setType(request.getType());
    }
    if (request.getPrice() != null) {
      license.setPrice(request.getPrice());
    }
    if (request.getRights() != null) {
      license.setRights(request.getRights());
    }

    return licenseRepository.save(license);
  }

  @Override
  @Transactional
  public void deleteLicense(Long id) {
    if (!licenseRepository.existsById(id)) {
      throw new EntityNotFoundException("License not found");
    }
    licenseRepository.deleteById(id);
  }

  @Override
  public License getLicense(Long id) {
    return licenseRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("License not found"));
  }
}