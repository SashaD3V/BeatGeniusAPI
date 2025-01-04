package com.WeAre.BeatGenius.domain.enums;

public enum Note {
  C("C"),
  C_SHARP("C#"),
  D("D"),
  D_SHARP("D#"),
  E("E"),
  F("F"),
  F_SHARP("F#"),
  G("G"),
  G_SHARP("G#"),
  A("A"),
  A_SHARP("A#"),
  B("B");

  private final String notation;

  Note(String notation) {
    this.notation = notation;
  }

  public String getNotation() {
    return notation;
  }
}
