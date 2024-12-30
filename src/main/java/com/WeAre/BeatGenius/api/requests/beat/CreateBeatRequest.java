package com.WeAre.BeatGenius.api.requests.beat;

import com.WeAre.BeatGenius.domain.enums.Genre;
import lombok.Data;

@Data
public class CreateBeatRequest {
    private String title;
    private String audioUrl;
    private Double price;
    private Genre genre;
}