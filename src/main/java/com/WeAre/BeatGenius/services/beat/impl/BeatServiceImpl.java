package com.WeAre.BeatGenius.services.beat.impl;

import com.WeAre.BeatGenius.domain.exceptions.ForbiddenException;
import com.WeAre.BeatGenius.domain.exceptions.UnauthorizedException;
import org.springframework.security.core.userdetails.UserDetails;
import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.exceptions.ResourceNotFoundException;
import com.WeAre.BeatGenius.domain.mappers.BeatMapper;
import com.WeAre.BeatGenius.domain.repositories.BeatRepository;
import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import com.WeAre.BeatGenius.services.generic.impl.GenericServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeatServiceImpl extends GenericServiceImpl<Beat, BeatResponse, CreateBeatRequest, UpdateBeatRequest>
        implements BeatService {
    private final UserRepository userRepository;

    public BeatServiceImpl(BeatRepository repository, BeatMapper mapper, UserRepository userRepository) {
        super(repository, mapper);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BeatResponse create(CreateBeatRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        Object principal = authentication.getPrincipal();
        System.out.println("Principal: " + principal);

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            System.out.println("UserDetails: " + userDetails);

            Long producerId;
            if (userDetails instanceof User) {
                producerId = ((User) userDetails).getId();
            } else {
                producerId = Long.parseLong(userDetails.getUsername());
            }

            System.out.println("Producer ID: " + producerId);

            User producer = userRepository.findById(producerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));

            Beat beat = mapper.toEntity(request);
            beat.setProducer(producer);

            Beat savedBeat = repository.save(beat);
            return mapper.toDto(savedBeat);
        } else {
            throw new UnauthorizedException("Utilisateur non authentifié ou principal non reconnu");
        }
    }

    @Override
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Vous devez être connecté");
        }

        User currentUser = (User) authentication.getPrincipal();
        Beat beat = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

        if (!beat.getProducer().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Vous n'êtes pas autorisé à supprimer ce beat");
        }

        repository.deleteById(id);
    }

    @Override
    public Page<BeatResponse> getAll(Pageable pageable) {
        System.out.println("Pageable request: " + pageable);
        Page<Beat> beats = repository.findAll(pageable);
        System.out.println("Found beats: " + beats.getContent());
        return beats.map(mapper::toDto);
    }

    @Override
    public Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable) {
        System.out.println("Getting beats for producer: " + producerId);
        System.out.println("Pageable request: " + pageable);

        // Vérifie si les beats existent pour ce producteur
        Page<Beat> beats = ((BeatRepository) repository).findByProducerId(producerId, pageable);
        System.out.println("Found beats in DB: " + beats.getContent()); // Pour debug

        return beats.map(mapper::toDto);
    }
}