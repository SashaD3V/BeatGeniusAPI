package com.WeAre.BeatGenius.services.beat.interfaces;

import com.WeAre.BeatGenius.api.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.responses.beat.BeatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeatService {
    BeatResponse createBeat(CreateBeatRequest request, Long producerId);
    BeatResponse updateBeat(Long id, UpdateBeatRequest request);
    BeatResponse getBeat(Long id);
    Page<BeatResponse> getAllBeats(Pageable pageable);
    void deleteBeat(Long id);
    Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable);
}