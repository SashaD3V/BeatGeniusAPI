package com.WeAre.BeatGenius.domain.repositories;

import com.WeAre.BeatGenius.domain.entities.BeatCredit;
import com.WeAre.BeatGenius.domain.enums.CreditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeatCreditRepository extends JpaRepository<BeatCredit, Long> {
    List<BeatCredit> findByBeatId(Long beatId);
    List<BeatCredit> findByProducerId(Long producerId);
    Optional<BeatCredit> findByBeatIdAndProducerId(Long beatId, Long producerId);
    List<BeatCredit> findByBeatIdAndStatus(Long beatId, CreditStatus status);

    @Query("SELECT SUM(bc.profitShare) FROM BeatCredit bc WHERE bc.beat.id = :beatId")
    Double sumProfitSharesByBeatId(@Param("beatId") Long beatId);

    @Query("SELECT SUM(bc.publishingShare) FROM BeatCredit bc WHERE bc.beat.id = :beatId")
    Double sumPublishingSharesByBeatId(@Param("beatId") Long beatId);
}