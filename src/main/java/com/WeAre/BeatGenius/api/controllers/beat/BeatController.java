package com.WeAre.BeatGenius.api.controllers.beat;

import com.WeAre.BeatGenius.api.controllers.BaseController;
import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Beat Controller
@RestController
@RequestMapping("/api/v1/beats")
public class BeatController extends BaseController<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {
    private final BeatService beatService;

    public BeatController(BeatService service) {
        super(service);
        this.beatService = service;
    }

    // Gardez uniquement les endpoints spécifiques
    @GetMapping("/producer/{producerId}")
    public ResponseEntity<Page<BeatResponse>> getProducerBeats(@PathVariable Long producerId, Pageable pageable) {
        return ResponseEntity.ok(beatService.getProducerBeats(producerId, pageable));
    }
}