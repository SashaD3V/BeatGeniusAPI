package com.WeAre.BeatGenius.domain.repositories;

import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.enums.LicenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {
    List<License> findByBeatId(Long beatId);
    List<License> findByType(LicenseType type);
}