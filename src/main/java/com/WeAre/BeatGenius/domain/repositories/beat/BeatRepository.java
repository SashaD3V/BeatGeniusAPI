package com.WeAre.BeatGenius.domain.repositories.beat;

import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.enums.Genre;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeatRepository extends JpaRepository<Beat, Long> {
  Page<Beat> findByProducerId(Long producerId, Pageable pageable);

  List<Beat> findByGenre(Genre genre);
}
