package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.domain.enums.ErrorCode;

// BeatSecurityException.java
public class BeatSecurityException extends BaseGeniusException {
  public BeatSecurityException(String message, String technicalDetails) {
    super(ErrorCode.ACCESS_DENIED, message, technicalDetails, null);
  }
}
