package com.WeAre.BeatGenius.services.beat.interfaces;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.services.generic.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeatService
    extends BaseService<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {
  // Uniquement la méthode spécifique qui reste nécessaire
  Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable);
}
