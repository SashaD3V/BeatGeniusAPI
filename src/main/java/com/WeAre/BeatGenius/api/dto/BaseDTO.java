package com.WeAre.BeatGenius.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class BaseDTO {
    private Long id;
    private LocalDateTime createdAt;
}