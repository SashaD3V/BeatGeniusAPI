package com.WeAre.BeatGenius.domain.repositories;

import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BeatRepository extends JpaRepository<Beat, Long> {
    Page<Beat> findByProducerId(Long producerId, Pageable pageable);
    List<Beat> findByGenre(Genre genre);
}