package com.WeAre.BeatGenius.services.beat.impl;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseOptionRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.constants.CreditConstants;
import com.WeAre.BeatGenius.domain.constants.LicenseConstants;
import com.WeAre.BeatGenius.domain.constants.PricingConstants;
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
  private final BeatCreditRepository beatCreditRepository;

  public BeatServiceImpl(
      BeatRepository repository,
      BeatMapper mapper,
      UserRepository userRepository,
      LicenseService licenseService,
      BeatCreditRepository beatCreditRepository) {
    super(repository, mapper);
    this.userRepository = userRepository;
    this.licenseService = licenseService;
    this.beatCreditRepository = beatCreditRepository;
  }

  protected User getCurrentProducer() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Long producerId = ((User) userDetails).getId();

    return userRepository
        .findById(producerId)
        .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));
  }

  protected Beat createAndSaveBeat(CreateBeatRequest request, User producer) {
    Beat beat = mapper.toEntity(request);
    beat.setProducer(producer);
    Beat savedBeat = repository.save(beat);
    repository.flush();
    return savedBeat;
  }

  protected void addStandardLicenses(Beat beat) {
    License basicLicense = createStandardLicense(LicenseType.BASIC, beat);
    License premiumLicense = createStandardLicense(LicenseType.PREMIUM, beat);
    License exclusiveLicense = createStandardLicense(LicenseType.EXCLUSIVE, beat);

    beat.getLicenses().add(basicLicense);
    beat.getLicenses().add(premiumLicense);
    beat.getLicenses().add(exclusiveLicense);

    repository.save(beat);
  }

  protected void addMainProducerCredit(Beat beat, User producer) {
    BeatCredit mainProducerCredit = new BeatCredit();
    mainProducerCredit.setBeat(beat);
    mainProducerCredit.setProducer(producer);
    mainProducerCredit.setRole(CreditConstants.MAIN_PRODUCER_ROLE);
    mainProducerCredit.setProfitShare(CreditConstants.MAIN_PRODUCER_PROFIT_SHARE);
    mainProducerCredit.setPublishingShare(CreditConstants.MAIN_PRODUCER_PUBLISHING_SHARE);
    mainProducerCredit.setStatus(CreditStatus.ACCEPTED);

    beatCreditRepository.save(mainProducerCredit);
  }

  @Override
  @Transactional
  public BeatResponse create(CreateBeatRequest request) {
    // 1. Récupérer le producteur courant
    User producer = getCurrentProducer();

    // 2. Créer et sauvegarder le beat
    Beat savedBeat = createAndSaveBeat(request, producer);

    // 3. Ajouter les licences standard
    addStandardLicenses(savedBeat);

    // 4. Ajouter le crédit du producteur principal
    addMainProducerCredit(savedBeat, producer);

    // 5. Rafraîchir et retourner le beat
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
          case BASIC -> PricingConstants.BASIC_LICENSE_PRICE;
          case PREMIUM -> PricingConstants.PREMIUM_LICENSE_PRICE;
          case EXCLUSIVE -> PricingConstants.EXCLUSIVE_LICENSE_PRICE;
          default -> throw new IllegalArgumentException("Type de licence non supporté");
        });

    return licenseService.createStandardLicense(option, beat);
  }

  private void createDefaultLicenses(Beat beat) {
    CreateLicenseRequest basicLicense =
        CreateLicenseRequest.builder()
            .name("Basic License")
            .type(LicenseType.BASIC)
            .price(PricingConstants.BASIC_LICENSE_PRICE)
            .fileFormat(PricingConstants.BASIC_LICENSE_FORMAT)
            .rights("Non-exclusive rights")
            .isTagged(true)
            .contractTerms(LicenseConstants.BASIC_LICENSE_TERMS)
            .distributionLimit(PricingConstants.BASIC_LICENSE_DISTRIBUTION_LIMIT)
            .beatId(beat.getId())
            .build();

    CreateLicenseRequest premiumLicense =
        CreateLicenseRequest.builder()
            .name("Premium License")
            .type(LicenseType.PREMIUM)
            .price(PricingConstants.PREMIUM_LICENSE_PRICE)
            .fileFormat(PricingConstants.PREMIUM_LICENSE_FORMAT)
            .rights("Non-exclusive premium rights")
            .isTagged(false)
            .contractTerms(LicenseConstants.PREMIUM_LICENSE_TERMS)
            .distributionLimit(PricingConstants.PREMIUM_LICENSE_DISTRIBUTION_LIMIT)
            .beatId(beat.getId())
            .build();

    CreateLicenseRequest exclusiveLicense =
        CreateLicenseRequest.builder()
            .name("Exclusive Rights")
            .type(LicenseType.EXCLUSIVE)
            .price(PricingConstants.EXCLUSIVE_LICENSE_PRICE)
            .fileFormat(PricingConstants.EXCLUSIVE_LICENSE_FORMAT)
            .rights("Full exclusive rights")
            .isTagged(false)
            .contractTerms(LicenseConstants.EXCLUSIVE_LICENSE_TERMS)
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
