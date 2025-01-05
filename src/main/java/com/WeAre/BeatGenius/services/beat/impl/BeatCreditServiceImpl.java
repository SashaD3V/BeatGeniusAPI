package com.WeAre.BeatGenius.services.beat.impl;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatCreditResponse;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.entities.beat.BeatCredit;
import com.WeAre.BeatGenius.domain.enums.CreditStatus;
import com.WeAre.BeatGenius.domain.enums.UserRole;
import com.WeAre.BeatGenius.domain.exceptions.ForbiddenException;
import com.WeAre.BeatGenius.domain.exceptions.ResourceNotFoundException;
import com.WeAre.BeatGenius.domain.mappers.beat.BeatCreditMapper;
import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatCreditRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatRepository;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatCreditService;
import com.WeAre.BeatGenius.services.generic.impl.BaseServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BeatCreditServiceImpl
    extends BaseServiceImpl<
        BeatCredit, BeatCreditResponse, CreateBeatCreditRequest, UpdateBeatCreditRequest>
    implements BeatCreditService {

  private final BeatCreditRepository beatCreditRepository;
  private final BeatRepository beatRepository;
  private final UserRepository userRepository;
  private final BeatCreditMapper beatCreditMapper;

  public BeatCreditServiceImpl(
      BeatCreditRepository beatCreditRepository,
      BeatRepository beatRepository,
      UserRepository userRepository,
      BeatCreditMapper beatCreditMapper) {
    super(beatCreditRepository, beatCreditMapper);
    this.beatCreditRepository = beatCreditRepository;
    this.beatRepository = beatRepository;
    this.userRepository = userRepository;
    this.beatCreditMapper = beatCreditMapper;
  }

  @Override
  @Transactional
  public List<BeatCreditResponse> addCredits(Long beatId, List<CreateBeatCreditRequest> requests) {
    Beat beat =
        beatRepository
            .findById(beatId)
            .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

    // Valider que la somme totale des parts ne dépasse pas 100%
    validateTotalShares(requests);

    // Valider que tous les producers ont bien le rôle PRODUCER
    requests.forEach(
        request -> {
          User producer =
              userRepository
                  .findById(request.getProducerId())
                  .orElseThrow(
                      () ->
                          new ResourceNotFoundException(
                              "Producer not found with id: " + request.getProducerId()));

          if (producer.getRole() != UserRole.PRODUCER) {
            throw new ForbiddenException(
                "User with id "
                    + request.getProducerId()
                    + " must have PRODUCER role to be added as a credit");
          }
        });

    return requests.stream()
        .map(request -> addSingleCredit(beat, request))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public BeatCreditResponse validateCredit(Long creditId, boolean accept) {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    BeatCredit credit =
        beatCreditRepository
            .findById(creditId)
            .orElseThrow(() -> new ResourceNotFoundException("Credit not found"));

    // Vérifier que l'utilisateur actuel est le producteur principal du beat
    if (!credit.getBeat().getProducer().getId().equals(currentUser.getId())) {
      throw new ForbiddenException("Only the main producer can validate credits");
    }

    credit.setStatus(accept ? CreditStatus.ACCEPTED : CreditStatus.REJECTED);
    credit.setValidationDate(LocalDateTime.now());
    credit.setUpdatedAt(LocalDateTime.now());

    return beatCreditMapper.toDto(beatCreditRepository.save(credit));
  }

  @Override
  @Transactional
  public BeatCreditResponse update(Long id, UpdateBeatCreditRequest updateDto) {
    BeatCredit credit =
        beatCreditRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Credit not found"));

    // Vérifier que c'est bien le producteur du crédit qui fait la mise à jour
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!credit.getProducer().getId().equals(currentUser.getId())) {
      throw new ForbiddenException(
          "Only the credited producer can update their own credit details");
    }

    // On met à jour avec les nouvelles données
    beatCreditMapper.updateEntityFromDto(updateDto, credit);

    // Toute modification remet le crédit en PENDING
    log.info("Setting credit {} back to PENDING due to update", id);
    credit.setStatus(CreditStatus.PENDING);
    credit.setValidationDate(null);
    credit.setUpdatedAt(LocalDateTime.now());

    // Valider que la somme des parts ne dépasse pas 100%
    validateSharesForUpdate(credit);

    return beatCreditMapper.toDto(beatCreditRepository.save(credit));
  }

  @Override
  public List<BeatCreditResponse> getPendingBeatCredits(Long beatId) {
    return beatCreditRepository.findByBeatIdAndStatus(beatId, CreditStatus.PENDING).stream()
        .map(beatCreditMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<BeatCreditResponse> getBeatCredits(Long beatId) {
    return beatCreditRepository.findByBeatId(beatId).stream()
        .map(beatCreditMapper::toDto)
        .collect(Collectors.toList());
  }

  private BeatCreditResponse addSingleCredit(Beat beat, CreateBeatCreditRequest request) {
    User producer =
        userRepository
            .findById(request.getProducerId())
            .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));

    BeatCredit credit = beatCreditMapper.toEntity(request);
    credit.setBeat(beat);
    credit.setProducer(producer);
    credit.setCreatedAt(LocalDateTime.now());
    credit.setUpdatedAt(LocalDateTime.now());
    credit.setStatus(CreditStatus.PENDING); // Toujours commence en PENDING

    return beatCreditMapper.toDto(beatCreditRepository.save(credit));
  }

  private void validateTotalShares(List<CreateBeatCreditRequest> requests) {
    double totalProfitShare =
        requests.stream().mapToDouble(CreateBeatCreditRequest::getProfitShare).sum();

    double totalPublishingShare =
        requests.stream().mapToDouble(CreateBeatCreditRequest::getPublishingShare).sum();

    if (totalProfitShare > 100 || totalPublishingShare > 100) {
      throw new ForbiddenException("Total shares cannot exceed 100%");
    }
  }

  private void validateSharesForUpdate(BeatCredit updatedCredit) {
    // Récupérer la somme des parts existantes pour ce beat
    Double currentProfitShare =
        beatCreditRepository.sumProfitSharesByBeatId(updatedCredit.getBeat().getId());
    Double currentPublishingShare =
        beatCreditRepository.sumPublishingSharesByBeatId(updatedCredit.getBeat().getId());

    // Soustraire les anciennes parts du crédit qu'on met à jour
    BeatCredit oldCredit = beatCreditRepository.findById(updatedCredit.getId()).orElse(null);
    if (oldCredit != null) {
      currentProfitShare -= oldCredit.getProfitShare();
      currentPublishingShare -= oldCredit.getPublishingShare();
    }

    // Vérifier que les nouvelles parts ne dépassent pas 100%
    if ((currentProfitShare + updatedCredit.getProfitShare() > 100)
        || (currentPublishingShare + updatedCredit.getPublishingShare() > 100)) {
      throw new ForbiddenException("Total shares cannot exceed 100%");
    }
  }
}
