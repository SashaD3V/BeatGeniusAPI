package com.WeAre.BeatGenius.api.dto.responses.beat;

import com.WeAre.BeatGenius.api.dto.BaseDTO;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BeatResponse extends BaseDTO {
    @Schema(example = "Trap Beat #1")
    private String title;

    @Schema(example = "https://storage.beatgenius.com/beats/trap-beat-1.mp3")
    private String audioUrl;

    @Schema(example = "29.99")
    private Double price;

    @Schema(example = "TRAP")
    private Genre genre;

    @Schema(example = "{\"id\": 1, \"artistName\": \"SashaBRRR\", \"email\": \"producer@example.com\"}")
    private UserResponse producer;
}