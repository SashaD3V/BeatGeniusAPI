package com.WeAre.BeatGenius.domain.enums;

public enum LicenseType {
  BASIC("Basic License"),
  PREMIUM("Premium License"),
  EXCLUSIVE("Exclusive Rights");

  private final String displayName;

  LicenseType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
