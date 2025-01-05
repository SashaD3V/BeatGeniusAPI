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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl
    extends BaseServiceImpl<License, LicenseResponse, CreateLicenseRequest, UpdateLicenseRequest>
    implements LicenseService {

  private final LicenseMapper licenseMapper;
  private final BeatRepository beatRepository;
  private final LicenseRepository licenseRepository;

  public LicenseServiceImpl(
      LicenseRepository licenseRepository,
      LicenseMapper licenseMapper,
      BeatRepository beatRepository) {
    super(licenseRepository, licenseMapper);
    this.licenseMapper = licenseMapper;
    this.beatRepository = beatRepository;
    this.licenseRepository = licenseRepository;
  }

  @Override
  @Transactional
  public License createStandardLicense(CreateLicenseOptionRequest option, Beat beat) {
    License license =
        License.builder().beat(beat).price(option.getPrice()).type(option.getType()).build();

    switch (option.getType()) {
      case BASIC -> {
        license.setName("Basic License");
        license.setFileFormat("MP3");
        license.setIsTagged(true);
        license.setDistributionLimit(10000);
        license.setRights("Non-exclusive rights");
        license.setContractTerms(
            """
                - MP3 file only
                - Distribution up to 10,000 copies
                - Must credit producer
                - For non-profit use only
                - Audio is tagged with producer tag
                """);
      }
      case PREMIUM -> {
        license.setName("Premium License");
        license.setFileFormat("WAV + MP3");
        license.setIsTagged(false);
        license.setDistributionLimit(100000);
        license.setRights("Non-exclusive premium rights");
        license.setContractTerms(
            """
                - WAV + MP3 files
                - Distribution up to 100,000 copies
                - Must credit producer
                - Commercial use allowed
                - Untagged audio files
                """);
      }
      case EXCLUSIVE -> {
        license.setName("Exclusive Rights");
        license.setFileFormat("WAV + MP3 + Trackouts");
        license.setIsTagged(false);
        license.setDistributionLimit(null);
        license.setRights("Full exclusive rights");
        license.setContractTerms(
            """
                - WAV + MP3 + Trackout files
                - Unlimited distribution
                - Full exclusive rights
                - Commercial use allowed
                - Beat will be removed from marketplace
                - Complete ownership transfer
                """);
      }
    }

    return repository.save(license);
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
