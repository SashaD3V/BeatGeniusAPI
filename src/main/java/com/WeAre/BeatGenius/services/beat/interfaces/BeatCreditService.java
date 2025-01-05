package com.WeAre.BeatGenius.services.beat.interfaces;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatCreditResponse;
import com.WeAre.BeatGenius.domain.entities.BeatCredit;
import com.WeAre.BeatGenius.services.generic.interfaces.BaseService;

import java.util.List;

public interface BeatCreditService extends BaseService<BeatCredit, BeatCreditResponse, CreateBeatCreditRequest, UpdateBeatCreditRequest> {
    List<BeatCreditResponse> addCredits(Long beatId, List<CreateBeatCreditRequest> requests);
    BeatCreditResponse validateCredit(Long creditId, boolean accept);
    List<BeatCreditResponse> getPendingBeatCredits(Long beatId);
    List<BeatCreditResponse> getBeatCredits(Long beatId);
}