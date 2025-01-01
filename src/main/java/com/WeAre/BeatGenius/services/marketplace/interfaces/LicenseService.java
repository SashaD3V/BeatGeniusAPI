package com.WeAre.BeatGenius.services.marketplace.interfaces;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateLicenseRequest;
import com.WeAre.BeatGenius.domain.entities.License;

public interface LicenseService {
    /**
     * Creates a new license
     *
     * @param request License creation details
     * @return The created license
     * @throws EntityNotFoundException if the beat is not found
     */
    License createLicense(CreateLicenseRequest request);

    /**
     * Updates an existing license
     *
     * @param id License ID to update
     * @param request Updated license details
     * @return The updated license
     * @throws EntityNotFoundException if the license is not found
     */
    License updateLicense(Long id, UpdateLicenseRequest request);

    /**
     * Deletes a license
     *
     * @param id License ID to delete
     * @throws EntityNotFoundException if the license is not found
     */
    void deleteLicense(Long id);

    /**
     * Gets a license by ID
     *
     * @param id License ID to find
     * @return The found license
     * @throws EntityNotFoundException if the license is not found
     */
    License getLicense(Long id);
}