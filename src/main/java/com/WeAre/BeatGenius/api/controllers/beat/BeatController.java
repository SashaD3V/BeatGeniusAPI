package com.WeAre.BeatGenius.api.controllers.beat;

import com.WeAre.BeatGenius.api.controllers.BaseController;
import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/beats")
public class BeatController extends BaseController<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {
    private final BeatService beatService;

    public BeatController(BeatService service) {
        super(service);
        this.beatService = service;
    }

    @Override
    @GetMapping
    @Operation(summary = "Obtenir tous les beats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des beats récupérée"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    public ResponseEntity<Page<BeatResponse>> getAll(
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de la page") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Nombre d'éléments par page") int size,
            @RequestParam(defaultValue = "createdAt,desc")
            @Parameter(
                    description = "Format: champ,direction",
                    schema = @Schema(type = "string", example = "createdAt,desc")
            ) String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(service.getAll(pageRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer un beat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Beat supprimé avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "404", description = "Beat non trouvé")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producer/{producerId}")
    @Operation(
            summary = "Obtenir les beats d'un producteur",
            description = """
                    Récupère les beats d'un producteur avec pagination et tri.
                    
                    Options de tri disponibles :
                    - title,asc/desc (Tri par titre)
                    - createdAt,asc/desc (Tri par date de création)
                    - price,asc/desc (Tri par prix)
                    - genre,asc/desc (Tri par genre)
                    
                    Exemple : /api/v1/beats/producer/5?page=0&size=10&sort=createdAt,desc
                    """
    )
    public ResponseEntity<Page<BeatResponse>> getProducerBeats(
            @PathVariable Long producerId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de la page") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Nombre d'éléments par page") int size,
            @RequestParam(defaultValue = "createdAt,desc")
            @Parameter(
                    description = "Format: champ,direction",
                    schema = @Schema(
                            type = "string",
                            example = "createdAt,desc",
                            description = "Valeurs possibles: title,asc/desc | createdAt,asc/desc | price,asc/desc | genre,asc/desc"
                    )
            ) String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(beatService.getProducerBeats(producerId, pageRequest));
    }
}