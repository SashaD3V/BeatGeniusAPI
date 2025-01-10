// LicenseTemplateService.java
package com.WeAre.BeatGenius.services.marketplace.impl;

import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.enums.LicenseType;
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseTemplateService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class LicenseTemplateServiceImpl implements LicenseTemplateService {

  protected abstract static class LicenseTemplate {
    protected final Beat beat;

    protected LicenseTemplate(Beat beat) {
      this.beat = beat;
    }

    public final License createLicense() {
      return License.builder()
          .beat(beat)
          .type(getLicenseType())
          .price(getPrice())
          .name(getName())
          .fileFormat(getFileFormat())
          .isTagged(isTagged())
          .distributionLimit(getDistributionLimit())
          .rights(getRights())
          .contractTerms(getContractTerms())
          .build();
    }

    protected abstract LicenseType getLicenseType();

    protected abstract BigDecimal getPrice();

    protected abstract String getName();

    protected abstract String getFileFormat();

    protected abstract boolean isTagged();

    protected abstract Integer getDistributionLimit();

    protected abstract String getRights();

    protected abstract String getContractTerms();
  }

  private class BasicLicenseTemplate extends LicenseTemplate {
    public BasicLicenseTemplate(Beat beat) {
      super(beat);
    }

    @Override
    protected LicenseType getLicenseType() {
      return LicenseType.BASIC;
    }

    @Override
    protected BigDecimal getPrice() {
      return new BigDecimal("29.99");
    }

    @Override
    protected String getName() {
      return "Basic License";
    }

    @Override
    protected String getFileFormat() {
      return "MP3";
    }

    @Override
    protected boolean isTagged() {
      return true;
    }

    @Override
    protected Integer getDistributionLimit() {
      return 10000;
    }

    @Override
    protected String getRights() {
      return "Non-exclusive rights";
    }

    @Override
    protected String getContractTerms() {
      return """
                - MP3 file only
                - Distribution up to 10,000 copies
                - Must credit producer
                - For non-profit use only
                - Audio is tagged with producer tag
                """;
    }
  }

  private class PremiumLicenseTemplate extends LicenseTemplate {
    public PremiumLicenseTemplate(Beat beat) {
      super(beat);
    }

    @Override
    protected LicenseType getLicenseType() {
      return LicenseType.PREMIUM;
    }

    @Override
    protected BigDecimal getPrice() {
      return new BigDecimal("99.99");
    }

    @Override
    protected String getName() {
      return "Premium License";
    }

    @Override
    protected String getFileFormat() {
      return "WAV + MP3";
    }

    @Override
    protected boolean isTagged() {
      return false;
    }

    @Override
    protected Integer getDistributionLimit() {
      return 100000;
    }

    @Override
    protected String getRights() {
      return "Non-exclusive premium rights";
    }

    @Override
    protected String getContractTerms() {
      return """
                - WAV + MP3 files
                - Distribution up to 100,000 copies
                - Must credit producer
                - Commercial use allowed
                - Untagged audio files
                """;
    }
  }

  private class ExclusiveLicenseTemplate extends LicenseTemplate {
    public ExclusiveLicenseTemplate(Beat beat) {
      super(beat);
    }

    @Override
    protected LicenseType getLicenseType() {
      return LicenseType.EXCLUSIVE;
    }

    @Override
    protected BigDecimal getPrice() {
      return new BigDecimal("999.99");
    }

    @Override
    protected String getName() {
      return "Exclusive Rights";
    }

    @Override
    protected String getFileFormat() {
      return "WAV + MP3 + Trackouts";
    }

    @Override
    protected boolean isTagged() {
      return false;
    }

    @Override
    protected Integer getDistributionLimit() {
      return null;
    }

    @Override
    protected String getRights() {
      return "Full exclusive rights";
    }

    @Override
    protected String getContractTerms() {
      return """
                - WAV + MP3 + Trackout files
                - Unlimited distribution
                - Full exclusive rights
                - Commercial use allowed
                - Beat will be removed from marketplace
                - Complete ownership transfer
                """;
    }
  }

  public License createLicenseFromTemplate(LicenseType type, Beat beat) {
    LicenseTemplate template =
        switch (type) {
          case BASIC -> new BasicLicenseTemplate(beat);
          case PREMIUM -> new PremiumLicenseTemplate(beat);
          case EXCLUSIVE -> new ExclusiveLicenseTemplate(beat);
        };
    return template.createLicense();
  }
}
