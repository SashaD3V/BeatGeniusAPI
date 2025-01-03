package com.WeAre.BeatGenius.api.dto.requests.beat;

import com.WeAre.BeatGenius.domain.enums.Genre;
import lombok.Data;

@Data
public class UpdateBeatRequest {
    private String title;
    private Double price;
    private Genre genre;
}
