package com.WeAre.BeatGenius.api.controllers.generic;

import com.WeAre.BeatGenius.api.dto.generic.BaseDTO;
import com.WeAre.BeatGenius.api.dto.responses.page.PageResponse;
import com.WeAre.BeatGenius.domain.entities.BaseEntity;
import com.WeAre.BeatGenius.services.generic.interfaces.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class BaseController<E extends BaseEntity, D extends BaseDTO, C, U> {

  protected final BaseService<E, D, C, U> service;

  protected BaseController(BaseService<E, D, C, U> service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Créer une nouvelle entité")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Entité créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
      })
  public ResponseEntity<D> create(@RequestBody C createDto) {
    return ResponseEntity.ok(service.create(createDto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Mettre à jour une entité")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Entité mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
      })
  public ResponseEntity<D> update(@PathVariable Long id, @RequestBody U updateDto) {
    return ResponseEntity.ok(service.update(id, updateDto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtenir une entité par son ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Entité trouvée"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
      })
  public ResponseEntity<D> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @GetMapping
  @Operation(summary = "Obtenir toutes les entités")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Liste des entités récupérée")})
  public ResponseEntity<PageResponse<D>> getAll(
      @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de la page") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "Nombre d'éléments par page")
          int size,
      @RequestParam(defaultValue = "createdAt,desc")
          @Parameter(
              description = "Format: champ,direction",
              schema = @Schema(type = "string", example = "createdAt,desc"))
          String sort) {
    String[] sortParams = sort.split(",");
    Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
    PageRequest pageRequest = PageRequest.of(page, size, sortObj);
    return ResponseEntity.ok(PageResponse.from(service.getAll(pageRequest)));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Supprimer une entité")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Entité supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
      })
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
