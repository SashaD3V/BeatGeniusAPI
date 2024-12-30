package com.WeAre.BeatGenius.api.controllers.beat;

import com.WeAre.BeatGenius.api.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/beats")
@Tag(name = "Beat Management", description = "Endpoints pour la gestion des beats")
@RequiredArgsConstructor
public class BeatController {
    private final BeatService beatService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUCER', 'ADMIN')")
    public ResponseEntity<BeatResponse> createBeat(
            @RequestBody CreateBeatRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(beatService.createBeat(request, user.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUCER', 'ADMIN')")
    public ResponseEntity<Void> deleteBeat(@PathVariable Long id, Authentication authentication) {
        // Vérifier que le producer est propriétaire du beat ou est admin
        beatService.deleteBeat(id);
        return ResponseEntity.noContent().build();
    }
}