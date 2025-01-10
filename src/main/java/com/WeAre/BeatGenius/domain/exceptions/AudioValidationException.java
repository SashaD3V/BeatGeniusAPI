package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.domain.enums.ErrorCode;

// AudioValidationException.java
public class AudioValidationException extends BaseGeniusException {
  public AudioValidationException(String message, String technicalDetails) {
    super(ErrorCode.INVALID_AUDIO_QUALITY, message, technicalDetails, null);
  }
}
