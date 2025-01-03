package com.WeAre.BeatGenius.api.dto.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseResponse {
    private Long id;
    private LocalDateTime createdAt;
}