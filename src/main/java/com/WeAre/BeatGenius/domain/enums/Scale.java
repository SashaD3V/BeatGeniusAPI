package com.WeAre.BeatGenius.domain.enums;

public enum Scale {
  MAJOR("Major"),
  MINOR("Minor");

  private final String name;

  Scale(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
