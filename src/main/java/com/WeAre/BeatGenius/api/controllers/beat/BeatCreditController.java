package com.WeAre.BeatGenius.api.controllers.beat;

import com.WeAre.BeatGenius.api.controllers.generic.BaseController;
import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatCreditResponse;
import com.WeAre.BeatGenius.api.dto.responses.page.PageResponse;
import com.WeAre.BeatGenius.domain.entities.beat.BeatCredit;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatCreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/beats/{beatId}/credits")
@PreAuthorize("hasAuthority('ROLE_PRODUCER')")
public class BeatCreditController
    extends BaseController<
        BeatCredit, BeatCreditResponse, CreateBeatCreditRequest, UpdateBeatCreditRequest> {

  private final BeatCreditService beatCreditService;

  public BeatCreditController(BeatCreditService service) {
    super(service);
    this.beatCreditService = service;
  }

  @Override
  @PostMapping
  public ResponseEntity<BeatCreditResponse> create(@RequestBody CreateBeatCreditRequest createDto) {
    throw new UnsupportedOperationException("Use addCredits instead");
  }

  @Override
  @GetMapping("/all")
  @Operation(summary = "Obtenir tous les crédits (paginés)")
  public ResponseEntity<PageResponse<BeatCreditResponse>> getAll(
      @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de la page") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "Nombre d'éléments par page")
          int size,
      @RequestParam(defaultValue = "createdAt,desc")
          @Parameter(description = "Format: champ,direction")
          String sort) {
    return super.getAll(page, size, sort);
  }

  @PostMapping("/batch")
  @Operation(summary = "Ajouter des crédits à un beat")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Crédits ajoutés avec succès"),
        @ApiResponse(
            responseCode = "400",
            description = "Requête invalide - Total des parts > 100%"),
        @ApiResponse(responseCode = "403", description = "Non autorisé - Rôle PRODUCER requis"),
        @ApiResponse(responseCode = "404", description = "Beat non trouvé")
      })
  public ResponseEntity<List<BeatCreditResponse>> addCredits(
      @PathVariable Long beatId, @RequestBody List<CreateBeatCreditRequest> requests) {
    return ResponseEntity.ok(beatCreditService.addCredits(beatId, requests));
  }

  @Override
  @PutMapping("/{creditId}")
  @Operation(summary = "Mettre à jour un crédit")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Crédit mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Crédit non trouvé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
      })
  public ResponseEntity<BeatCreditResponse> update(
      @PathVariable Long creditId, @RequestBody UpdateBeatCreditRequest updateDto) {
    return super.update(creditId, updateDto);
  }

  @PutMapping("/{creditId}/validate")
  @Operation(summary = "Valider ou rejeter un crédit")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Statut du crédit mis à jour"),
        @ApiResponse(
            responseCode = "403",
            description = "Non autorisé - Seul le producteur concerné peut valider"),
        @ApiResponse(responseCode = "404", description = "Crédit non trouvé")
      })
  public ResponseEntity<BeatCreditResponse> validateCredit(
      @PathVariable Long beatId, @PathVariable Long creditId, @RequestParam boolean accept) {
    return ResponseEntity.ok(beatCreditService.validateCredit(creditId, accept));
  }

  @GetMapping("/pending")
  @Operation(summary = "Obtenir les crédits en attente de validation pour un beat")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Liste des crédits en attente"),
        @ApiResponse(responseCode = "404", description = "Beat non trouvé")
      })
  public ResponseEntity<List<BeatCreditResponse>> getPendingCredits(@PathVariable Long beatId) {
    return ResponseEntity.ok(beatCreditService.getPendingBeatCredits(beatId));
  }

  @GetMapping
  @Operation(summary = "Obtenir tous les crédits d'un beat spécifique")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Liste des crédits"),
        @ApiResponse(responseCode = "404", description = "Beat non trouvé")
      })
  public ResponseEntity<List<BeatCreditResponse>> getBeatCredits(@PathVariable Long beatId) {
    return ResponseEntity.ok(beatCreditService.getBeatCredits(beatId));
  }

  @Override
  @DeleteMapping("/{creditId}")
  @Operation(summary = "Supprimer un crédit")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Crédit supprimé avec succès"),
        @ApiResponse(
            responseCode = "403",
            description = "Non autorisé - Seul le propriétaire du beat peut supprimer"),
        @ApiResponse(responseCode = "404", description = "Crédit non trouvé")
      })
  public ResponseEntity<Void> delete(@PathVariable Long creditId) {
    return super.delete(creditId);
  }

  @Override
  @GetMapping("/{creditId}")
  @Operation(summary = "Obtenir un crédit par son ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Crédit trouvé"),
        @ApiResponse(responseCode = "404", description = "Crédit non trouvé")
      })
  public ResponseEntity<BeatCreditResponse> getById(@PathVariable Long creditId) {
    return super.getById(creditId);
  }
}
