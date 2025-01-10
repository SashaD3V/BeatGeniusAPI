package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.domain.enums.ErrorCode;

// AudioProcessingException.java
public class AudioProcessingException extends BaseGeniusException {
  public AudioProcessingException(String message, String technicalDetails, Throwable cause) {
    super(ErrorCode.AUDIO_PROCESSING_ERROR, message, technicalDetails, cause);
  }
}
