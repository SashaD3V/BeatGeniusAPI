package com.WeAre.BeatGenius.api.controllers.beat;

import com.WeAre.BeatGenius.api.controllers.BaseController;
import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.api.dto.responses.page.PageResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.Note;
import com.WeAre.BeatGenius.domain.enums.Scale;
import com.WeAre.BeatGenius.domain.exceptions.InvalidFileException;
import com.WeAre.BeatGenius.infrastructure.storage.StorageService;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/beats")
public class BeatController
    extends BaseController<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest> {
  private final BeatService beatService;
  private final StorageService storageService;

  public BeatController(BeatService service, StorageService storageService) {
    super(service);
    this.beatService = service;
    this.storageService = storageService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasAuthority('ROLE_PRODUCER')")
  @Operation(
      summary = "Créer un nouveau beat",
      description =
          """
                Upload d'une nouvelle instrumentale.

                Formats acceptés : MP3, WAV
                Durée : minimum 1 minute, maximum 5 minutes
                """)
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Instrumentale créée avec succès"),
        @ApiResponse(
            responseCode = "400",
            description = "Fichier invalide (format ou durée non conforme)"),
        @ApiResponse(responseCode = "401", description = "Non autorisé - Token requis"),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle PRODUCER requis")
      })
  public ResponseEntity<BeatResponse> create(
      @RequestParam @Parameter(description = "Titre du beat") String title,
      @RequestParam @Parameter(description = "Genre musical") Genre genre,
      @RequestParam(required = false) @Parameter(description = "Description") String description,
      @RequestParam(required = false) @Parameter(description = "BPM") Integer bpm,
      @RequestParam(required = false)
          @Parameter(
              description = "Note fondamentale",
              schema = @Schema(implementation = Note.class))
          Note note,
      @RequestParam(required = false)
          @Parameter(
              description = "Gamme (Major/Minor)",
              schema = @Schema(implementation = Scale.class))
          Scale scale,
      @RequestParam(required = false) @Parameter(description = "Tags") List<String> tags,
      @RequestParam(required = false) @Parameter(description = "Ambiances") List<String> moods,
      @RequestParam(required = false) @Parameter(description = "Instruments")
          List<String> instruments,
      @RequestParam(required = false)
          @Parameter(
              description = "Date de sortie",
              schema =
                  @Schema(
                      type = "string",
                      format = "date",
                      example = "2025-01-05") // format date uniquement
              )
          String releaseDateStr,
      @RequestParam(required = false) @Parameter(description = "Éligible aux réductions")
          Boolean includeForBulkDiscounts,
      @RequestPart(value = "file")
          @Parameter(
              description = "Fichier audio (MP3/WAV)",
              content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
          MultipartFile audioFile)
      throws IOException, UnsupportedAudioFileException {
    // Vérification du type MIME
    String contentType = audioFile.getContentType();
    if (!"audio/mpeg".equals(contentType) && !"audio/wav".equals(contentType)) {
      throw new InvalidFileException("Le fichier doit être au format MP3 ou WAV");
    }

    // Vérification de la durée avec AudioInputStream
    try (AudioInputStream audioInputStream =
        AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioFile.getBytes()))) {
      AudioFormat format = audioInputStream.getFormat();
      long frames = audioInputStream.getFrameLength();
      double durationInSeconds = (frames / format.getFrameRate());

      if (durationInSeconds < 60 || durationInSeconds > 300) {
        throw new InvalidFileException("La durée doit être entre 1 et 5 minutes");
      }

      String audioUrl = storageService.store(audioFile);

      // ICI - Conversion de la date
      LocalDateTime releaseDate = null;
      if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
        try {
          releaseDate = LocalDate.parse(releaseDateStr).atStartOfDay();
        } catch (DateTimeParseException e) {
          throw new IllegalArgumentException(
              "Format de date invalide. Utilisez le format: YYYY-MM-DD");
        }
      }

      // Création du DTO
      CreateBeatRequest createDto = new CreateBeatRequest();
      createDto.setTitle(title);
      createDto.setGenre(genre);
      createDto.setDescription(description);
      createDto.setAudioUrl(audioUrl);
      createDto.setBpm(bpm);
      createDto.setNote(note);
      createDto.setScale(scale);
      createDto.setTags(tags);
      createDto.setMoods(moods);
      createDto.setInstruments(instruments);
      createDto.setReleaseDate(releaseDate); // Utilisation de la date convertie
      createDto.setIncludeForBulkDiscounts(includeForBulkDiscounts);
      // heu on peut pas raccourcir un peu ce bloc de code ?

      return super.create(createDto);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Supprimer un beat")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Beat supprimé avec succès"),
        @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
        @ApiResponse(responseCode = "404", description = "Beat non trouvé")
      })
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping
  @Operation(summary = "Obtenir tous les beats")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Liste des beats récupérée"),
        @ApiResponse(responseCode = "401", description = "Non autorisé")
      })
  public ResponseEntity<PageResponse<BeatResponse>> getAll(
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

  @GetMapping("/producer/{producerId}")
  @Operation(
      summary = "Obtenir les beats d'un producteur",
      description =
          """
                   Récupère les beats d'un producteur avec pagination et tri.

                   Options de tri disponibles :
                   - title,asc/desc (Tri par titre)
                   - createdAt,asc/desc (Tri par date de création)
                   - price,asc/desc (Tri par prix)
                   - genre,asc/desc (Tri par genre)

                   Exemple : /api/v1/beats/producer/5?page=0&size=10&sort=createdAt,desc
                   """)
  public ResponseEntity<PageResponse<BeatResponse>> getProducerBeats(
      @PathVariable Long producerId,
      @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de la page") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "Nombre d'éléments par page")
          int size,
      @RequestParam(defaultValue = "createdAt,desc")
          @Parameter(
              description = "Format: champ,direction",
              schema =
                  @Schema(
                      type = "string",
                      example = "createdAt,desc",
                      description =
                          "Valeurs possibles: title,asc/desc | createdAt,asc/desc | price,asc/desc | genre,asc/desc"))
          String sort) {
    String[] sortParams = sort.split(",");
    Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
    PageRequest pageRequest = PageRequest.of(page, size, sortObj);
    return ResponseEntity.ok(
        PageResponse.from(beatService.getProducerBeats(producerId, pageRequest)));
  }
}
