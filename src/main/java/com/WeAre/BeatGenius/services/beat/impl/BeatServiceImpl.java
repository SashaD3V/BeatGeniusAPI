package com.WeAre.BeatGenius.services.beat.impl;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseOptionRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.entities.beat.BeatCredit;
import com.WeAre.BeatGenius.domain.enums.CreditStatus;
import com.WeAre.BeatGenius.domain.enums.LicenseType;
import com.WeAre.BeatGenius.domain.exceptions.ForbiddenException;
import com.WeAre.BeatGenius.domain.exceptions.ResourceNotFoundException;
import com.WeAre.BeatGenius.domain.exceptions.UnauthorizedException;
import com.WeAre.BeatGenius.domain.mappers.beat.BeatMapper;
import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatCreditRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatRepository;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import com.WeAre.BeatGenius.services.generic.impl.BaseServiceImpl;
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseService;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeatServiceImpl
    extends BaseServiceImpl<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest>
    implements BeatService {

  private final UserRepository userRepository;
  private final LicenseService licenseService;
  private final BeatCreditRepository beatCreditRepository; // Ajouter cette ligne

  public BeatServiceImpl(
      BeatRepository repository,
      BeatMapper mapper,
      UserRepository userRepository,
      LicenseService licenseService,
      BeatCreditRepository beatCreditRepository) { // Ajouter ce paramètre)
    super(repository, mapper);
    this.userRepository = userRepository;
    this.licenseService = licenseService;
    this.beatCreditRepository = beatCreditRepository; // Ajouter cette ligne
  }

  @Override
  @Transactional
  public BeatResponse create(CreateBeatRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Long producerId = ((User) userDetails).getId();

    User producer =
        userRepository
            .findById(producerId)
            .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));

    // 1. Créer et sauvegarder le beat d'abord
    Beat beat = mapper.toEntity(request);
    beat.setProducer(producer);
    Beat savedBeat = repository.save(beat);
    repository.flush(); // Force la synchronisation avec la base de données

    // 2. Créer et ajouter les licences standard
    License basicLicense = createStandardLicense(LicenseType.BASIC, savedBeat);
    License premiumLicense = createStandardLicense(LicenseType.PREMIUM, savedBeat);
    License exclusiveLicense = createStandardLicense(LicenseType.EXCLUSIVE, savedBeat);

    savedBeat.getLicenses().add(basicLicense);
    savedBeat.getLicenses().add(premiumLicense);
    savedBeat.getLicenses().add(exclusiveLicense);

    // 3. Ajouter automatiquement le crédit pour le producteur principal
    BeatCredit mainProducerCredit = new BeatCredit();
    mainProducerCredit.setBeat(savedBeat);
    mainProducerCredit.setProducer(producer);
    mainProducerCredit.setRole("Main Producer");
    mainProducerCredit.setProfitShare(50.0);
    mainProducerCredit.setPublishingShare(50.0);
    mainProducerCredit.setStatus(CreditStatus.ACCEPTED);

    beatCreditRepository.save(mainProducerCredit);

    // 4. Sauvegarder à nouveau le beat avec ses licences et crédits
    repository.save(savedBeat);

    // 5. Rafraîchir et retourner le résultat
    savedBeat =
        repository
            .findById(savedBeat.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

    return mapper.toDto(savedBeat);
  }

  private License createStandardLicense(LicenseType type, Beat beat) {
    CreateLicenseOptionRequest option = new CreateLicenseOptionRequest();
    option.setType(type);
    option.setPrice(
        switch (type) {
          case BASIC -> new BigDecimal("29.99");
          case PREMIUM -> new BigDecimal("99.99");
          case EXCLUSIVE -> new BigDecimal("999.99");
          default -> throw new IllegalArgumentException("Type de licence non supporté");
        });

    return licenseService.createStandardLicense(option, beat);
  }

  private void createDefaultLicenses(Beat beat) {
    CreateLicenseRequest basicLicense =
        CreateLicenseRequest.builder()
            .name("Basic License")
            .type(LicenseType.BASIC)
            .price(new BigDecimal("29.99"))
            .fileFormat("MP3")
            .rights("Non-exclusive rights")
            .isTagged(true)
            .contractTerms(
                """
                - MP3 file only
                - Distribution up to 10,000 copies
                - Must credit producer
                - For non-profit use only
                - Audio is tagged with producer tag
                """)
            .distributionLimit(10000)
            .beatId(beat.getId())
            .build();

    CreateLicenseRequest premiumLicense =
        CreateLicenseRequest.builder()
            .name("Premium License")
            .type(LicenseType.PREMIUM)
            .price(new BigDecimal("99.99"))
            .fileFormat("WAV + MP3")
            .rights("Non-exclusive premium rights")
            .isTagged(false)
            .contractTerms(
                """
                - WAV + MP3 files
                - Distribution up to 100,000 copies
                - Must credit producer
                - Commercial use allowed
                - Untagged audio files
                """)
            .distributionLimit(100000)
            .beatId(beat.getId())
            .build();

    CreateLicenseRequest exclusiveLicense =
        CreateLicenseRequest.builder()
            .name("Exclusive Rights")
            .type(LicenseType.EXCLUSIVE)
            .price(new BigDecimal("999.99"))
            .fileFormat("WAV + MP3 + Trackouts")
            .rights("Full exclusive rights")
            .isTagged(false)
            .contractTerms(
                """
                - WAV + MP3 + Trackout files
                - Unlimited distribution
                - Full exclusive rights
                - Commercial use allowed
                - Beat will be removed from marketplace
                - Complete ownership transfer
                """)
            .distributionLimit(null)
            .beatId(beat.getId())
            .build();

    licenseService.createLicense(basicLicense);
    licenseService.createLicense(premiumLicense);
    licenseService.createLicense(exclusiveLicense);
  }

  @Override
  @Transactional
  public BeatResponse update(Long id, UpdateBeatRequest request) {
    Beat existingBeat =
        repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Beat not found with id: " + id));

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    if (!existingBeat.getProducer().getId().equals(currentUser.getId())) {
      throw new ForbiddenException("Vous n'êtes pas autorisé à modifier ce beat");
    }

    mapper.updateEntityFromDto(request, existingBeat);
    Beat updatedBeat = repository.save(existingBeat);
    return mapper.toDto(updatedBeat);
  }

  @Override
  public void delete(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication.getPrincipal() instanceof User)) {
      throw new UnauthorizedException("Vous devez être connecté");
    }

    User currentUser = (User) authentication.getPrincipal();
    Beat beat =
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

    if (!beat.getProducer().getId().equals(currentUser.getId())) {
      throw new ForbiddenException("Vous n'êtes pas autorisé à supprimer ce beat");
    }

    repository.deleteById(id);
  }

  @Override
  public Page<BeatResponse> getAll(Pageable pageable) {
    Page<Beat> beats = repository.findAll(pageable);
    return beats.map(mapper::toDto);
  }

  @Override
  public Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable) {
    Page<Beat> beats = ((BeatRepository) repository).findByProducerId(producerId, pageable);
    return beats.map(mapper::toDto);
  }
}
