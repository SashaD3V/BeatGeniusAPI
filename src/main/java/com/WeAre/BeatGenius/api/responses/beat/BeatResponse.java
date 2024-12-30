package com.WeAre.BeatGenius.api.responses.beat;

import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.Genre;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BeatResponse {
    private Long id;
    private String title;
    private String audioUrl;
    private Double price;
    private Genre genre;
    private UserResponse producer;
    private LocalDateTime createdAt;
}