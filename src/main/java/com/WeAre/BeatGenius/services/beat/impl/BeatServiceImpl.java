package com.WeAre.BeatGenius.services.beat.impl;

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
import lombok.RequiredArgsConstructor;
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
        System.out.println("Authentication: " + authentication); // LOG

        // Récupérer l'objet UserDetails (qui est l'utilisateur authentifié)
        Object principal = authentication.getPrincipal();
        System.out.println("Principal: " + principal); // LOG

        // Vérifier si l'objet est une instance de UserDetails
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            System.out.println("UserDetails: " + userDetails); // LOG

            // Récupérer l'ID de l'utilisateur à partir de l'objet UserDetails
            Long producerId;
            if (userDetails instanceof User) {
                producerId = ((User) userDetails).getId();
            } else {
                // Si tu n'as pas directement un objet User, il faut extraire l'ID
                // d'une autre manière.
                // Exemple si l'ID est stocké dans le username (à adapter) :
                producerId = Long.parseLong(userDetails.getUsername());
            }

            System.out.println("Producer ID: " + producerId); // LOG

            // Le reste du code reste inchangé
            User producer = userRepository.findById(producerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));

            Beat beat = mapper.toEntity(request);
            beat.setProducer(producer);

            Beat savedBeat = repository.save(beat);
            return mapper.toDto(savedBeat);
        } else {
            // Gérer le cas où l'utilisateur n'est pas authentifié ou le principal n'est pas UserDetails
            throw new IllegalStateException("Utilisateur non authentifié ou principal non reconnu");
        }
    }

    @Override
    public Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable) {
        return ((BeatRepository) repository).findByProducerId(producerId, pageable)
                .map(mapper::toDto);
    }
}