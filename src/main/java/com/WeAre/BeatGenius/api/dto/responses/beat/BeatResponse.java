package com.WeAre.BeatGenius.api.dto.responses.beat;

import com.WeAre.BeatGenius.api.dto.BaseDTO;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.Genre;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeatResponse extends BaseDTO {
    private String title;
    private String audioUrl;
    private Double price;
    private Genre genre;
    private UserResponse producer;
}