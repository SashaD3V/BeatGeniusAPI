package com.WeAre.BeatGenius.domain.exceptions;

import com.WeAre.BeatGenius.domain.enums.ErrorCode;

// PaymentException.java
public class PaymentException extends BaseGeniusException {
  public PaymentException(String message, String technicalDetails) {
    super(ErrorCode.PAYMENT_FAILED, message, technicalDetails, null);
  }
}
