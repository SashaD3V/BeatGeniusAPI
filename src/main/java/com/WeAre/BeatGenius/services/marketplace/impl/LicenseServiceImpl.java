package com.WeAre.BeatGenius.services.marketplace.impl;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.mappers.LicenseMapper;
import com.WeAre.BeatGenius.domain.repositories.BeatRepository;
import com.WeAre.BeatGenius.domain.repositories.LicenseRepository;
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LicenseServiceImpl implements LicenseService {
    private final LicenseRepository licenseRepository;
    private final BeatRepository beatRepository;
    private final LicenseMapper licenseMapper;

    public LicenseServiceImpl(LicenseRepository licenseRepository,
                              BeatRepository beatRepository,
                              LicenseMapper licenseMapper) {
        this.licenseRepository = licenseRepository;
        this.beatRepository = beatRepository;
        this.licenseMapper = licenseMapper;
    }

    @Override
    public License createLicense(CreateLicenseRequest request) {
        License license = licenseMapper.toEntity(request);
        license.setBeat(beatRepository.findById(request.getBeatId())
                .orElseThrow(() -> new EntityNotFoundException("Beat not found")));
        return licenseRepository.save(license);
    }

    @Override
    public License updateLicense(Long id, UpdateLicenseRequest request) {
        License license = licenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("License not found"));

        license.setType(request.getType());
        license.setPrice(request.getPrice());
        license.setRights(request.getRights());

        return licenseRepository.save(license);
    }

    @Override
    public void deleteLicense(Long id) {
        if (!licenseRepository.existsById(id)) {
            throw new EntityNotFoundException("License not found");
        }
        licenseRepository.deleteById(id);
    }

    @Override
    public License getLicense(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("License not found"));
    }
}