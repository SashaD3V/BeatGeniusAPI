package com.WeAre.BeatGenius.api.controllers;

import com.WeAre.BeatGenius.api.dto.BaseDTO;
import com.WeAre.BeatGenius.domain.entities.BaseEntity;
import com.WeAre.BeatGenius.services.generic.interfaces.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class BaseController
        <E extends BaseEntity,
                D extends BaseDTO,
                C,
                U> {

protected final GenericService<E, D, C, U> service;

protected BaseController(GenericService<E, D, C, U> service) {
    this.service = service;
}

@PostMapping
@Operation(summary = "Créer une nouvelle entité")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entité créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
})
public ResponseEntity<D> create(@RequestBody C createDto) {
    return ResponseEntity.ok(service.create(createDto));
}

@PutMapping("/{id}")
@Operation(summary = "Mettre à jour une entité")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entité mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
})
public ResponseEntity<D> update(@PathVariable Long id, @RequestBody U updateDto) {
    return ResponseEntity.ok(service.update(id, updateDto));
}

@GetMapping("/{id}")
@Operation(summary = "Obtenir une entité par son ID")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entité trouvée"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
})
public ResponseEntity<D> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
}

@GetMapping
@Operation(summary = "Obtenir toutes les entités")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des entités récupérée")
})
public ResponseEntity<Page<D>> getAll(Pageable pageable) {
    return ResponseEntity.ok(service.getAll(pageable));
}

@DeleteMapping("/{id}")
@Operation(summary = "Supprimer une entité")
@ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Entité supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
})
public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
}
}