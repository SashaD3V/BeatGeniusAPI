package com.WeAre.BeatGenius.api.dto.responses.user;

import com.WeAre.BeatGenius.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponse {
  @Schema(example = "1")
  private Long id;

  @Schema(example = "producer@example.com")
  private String email;

  @Schema(example = "SashaBRRR")
  private String artistName;

  @Schema(example = "https://storage.beatgenius.com/avatars/producer1.jpg")
  private String profilePicture;

  @Schema(example = "PRODUCER")
  private UserRole role;
}
