package com.WeAre.BeatGenius.api.dto.responses.messaging;

import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageResponse {
  private Long id;
  private UserResponse sender;
  private UserResponse recipient;
  private String content;
  private LocalDateTime sentAt;
  private boolean read;
}
