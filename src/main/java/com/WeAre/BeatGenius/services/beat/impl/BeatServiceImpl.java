package com.WeAre.BeatGenius.services.beat.impl;

import com.WeAre.BeatGenius.api.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.requests.beat.UpdateBeatRequest;
import com.WeAre.BeatGenius.api.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.exceptions.ResourceNotFoundException;
import com.WeAre.BeatGenius.domain.mappers.BeatMapper;
import com.WeAre.BeatGenius.domain.repositories.BeatRepository;
import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import com.WeAre.BeatGenius.services.beat.interfaces.BeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BeatServiceImpl implements BeatService {
    private final BeatRepository beatRepository;
    private final UserRepository userRepository;
    private final BeatMapper beatMapper;

    @Override
    @Transactional
    public BeatResponse createBeat(CreateBeatRequest request, Long producerId) {
        User producer = userRepository.findById(producerId)
                .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));

        Beat beat = beatMapper.toEntity(request);
        beat.setProducer(producer);

        Beat savedBeat = beatRepository.save(beat);
        return beatMapper.toDto(savedBeat);
    }

    @Override
    @Transactional
    public BeatResponse updateBeat(Long id, UpdateBeatRequest request) {
        Beat beat = beatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));

        beatMapper.updateEntityFromDto(request, beat);
        Beat updatedBeat = beatRepository.save(beat);
        return beatMapper.toDto(updatedBeat);
    }

    @Override
    public BeatResponse getBeat(Long id) {
        Beat beat = beatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beat not found"));
        return beatMapper.toDto(beat);
    }

    @Override
    public Page<BeatResponse> getAllBeats(Pageable pageable) {
        return beatRepository.findAll(pageable)
                .map(beatMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteBeat(Long id) {
        if (!beatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Beat not found");
        }
        beatRepository.deleteById(id);
    }

    @Override
    public Page<BeatResponse> getProducerBeats(Long producerId, Pageable pageable) {
        return beatRepository.findByProducerId(producerId, pageable)
                .map(beatMapper::toDto);
    }

    // Autres implémentations...
}
