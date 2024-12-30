package com.WeAre.BeatGenius.api.responses.messaging;

import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private UserResponse sender;
    private UserResponse recipient;
    private String content;
    private LocalDateTime sentAt;
    private boolean read;
}