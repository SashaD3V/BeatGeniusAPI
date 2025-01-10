package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.domain.enums.ErrorCode;

// LicenseException.java
public class LicenseException extends BaseGeniusException {
  public LicenseException(String message, String technicalDetails) {
    super(ErrorCode.LICENSE_NOT_AVAILABLE, message, technicalDetails, null);
  }
}
