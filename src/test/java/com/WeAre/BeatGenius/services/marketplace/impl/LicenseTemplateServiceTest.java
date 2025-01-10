package com.WeAre.BeatGenius.services.marketplace.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.enums.LicenseType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LicenseTemplateServiceTest {

  @InjectMocks private LicenseTemplateServiceImpl licenseTemplateService;

  @Test
  void shouldCreateBasicLicense() {
    Beat beat = new Beat();
    License license = licenseTemplateService.createLicenseFromTemplate(LicenseType.BASIC, beat);

    assertNotNull(license);
    assertEquals(LicenseType.BASIC, license.getType());
    assertEquals("Basic License", license.getName());
    assertEquals("MP3", license.getFileFormat());
    assertTrue(license.getIsTagged());
    assertEquals(10000, license.getDistributionLimit());
    assertEquals(new BigDecimal("29.99"), license.getPrice());
  }

  @Test
  void shouldCreatePremiumLicense() {
    Beat beat = new Beat();
    License license = licenseTemplateService.createLicenseFromTemplate(LicenseType.PREMIUM, beat);

    assertNotNull(license);
    assertEquals(LicenseType.PREMIUM, license.getType());
    assertEquals("Premium License", license.getName());
    assertEquals("WAV + MP3", license.getFileFormat());
    assertFalse(license.getIsTagged());
    assertEquals(100000, license.getDistributionLimit());
    assertEquals(new BigDecimal("99.99"), license.getPrice());
  }

  @Test
  void shouldCreateExclusiveLicense() {
    Beat beat = new Beat();
    License license = licenseTemplateService.createLicenseFromTemplate(LicenseType.EXCLUSIVE, beat);

    assertNotNull(license);
    assertEquals(LicenseType.EXCLUSIVE, license.getType());
    assertEquals("Exclusive Rights", license.getName());
    assertEquals("WAV + MP3 + Trackouts", license.getFileFormat());
    assertFalse(license.getIsTagged());
    assertNull(license.getDistributionLimit());
    assertEquals(new BigDecimal("999.99"), license.getPrice());
  }
}
