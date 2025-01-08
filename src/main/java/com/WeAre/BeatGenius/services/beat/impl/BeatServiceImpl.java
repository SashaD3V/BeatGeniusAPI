package com.WeAre.BeatGenius.services.beat.impl;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateLicenseOptionRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.constants.CreditConstants;
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
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeatServiceImpl extends BaseServiceImpl<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest>
        implements BeatService {

  private final UserRepository userRepository;
  private final LicenseService licenseService;
  private final BeatCreditRepository beatCreditRepository;
  private final LicenseTemplateService licenseTemplateService;

  public BeatServiceImpl(
          BeatRepository repository,
          BeatMapper mapper,
          UserRepository userRepository,
          LicenseService licenseService,
          BeatCreditRepository beatCreditRepository,
          LicenseTemplateService licenseTemplateService) {
    super(repository, mapper);
    this.userRepository = userRepository;
    this.licenseService = licenseService;
    this.beatCreditRepository = beatCreditRepository;
    this.licenseTemplateService = licenseTemplateService;
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
    for (LicenseType type : LicenseType.values()) {
      License license = licenseTemplateService.createLicenseFromTemplate(type, beat);
      beat.getLicenses().add(license);
    }
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
    User producer = getCurrentProducer();
    Beat savedBeat = createAndSaveBeat(request, producer);

    addStandardLicenses(savedBeat);
    addMainProducerCredit(savedBeat, producer);

    savedBeat = repository
            .findById(savedBeat.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

    return mapper.toDto(savedBeat);
  }

  @Override
  @Transactional
  public BeatResponse update(Long id, UpdateBeatRequest request) {
    Beat existingBeat = repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Beat not found with id: " + id));

    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!existingBeat.getProducer().getId().equals(currentUser.getId())) {
      throw new ForbiddenException("Vous n'êtes pas autorisé à modifier ce beat");
    }

    mapper.updateEntityFromDto(request, existingBeat);
    return mapper.toDto(repository.save(existingBeat));
  }

  @Override
  public void delete(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication.getPrincipal() instanceof User)) {
      throw new UnauthorizedException("Vous devez être connecté");
    }

    User currentUser = (User) authentication.getPrincipal();
    Beat beat = repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

    if (!beat.getProducer().getId().equals(currentUser.getId())) {
      throw new ForbiddenException("Vous n'êtes pas autorisé à supprimer ce beat");
    }

    repository.deleteById(id);
  }

  @Override
  public Page<BeatResponse> getAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDto);
  }

  @Override
  public Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable) {
    return ((BeatRepository) repository).findByProducerId(producerId, pageable).map(mapper::toDto);
  }
}