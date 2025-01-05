package com.WeAre.BeatGenius.domain.mappers.beat;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.requests.beat.UpdateBeatCreditRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatCreditResponse;
import com.WeAre.BeatGenius.domain.entities.beat.BeatCredit;
import com.WeAre.BeatGenius.domain.mappers.BaseMapper;
import com.WeAre.BeatGenius.domain.mappers.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class})
public interface BeatCreditMapper
    extends BaseMapper<
        BeatCredit, BeatCreditResponse, CreateBeatCreditRequest, UpdateBeatCreditRequest> {

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "validationDate", ignore = true)
  @Mapping(target = "beat", ignore = true)
  @Mapping(target = "producer", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  BeatCredit toEntity(CreateBeatCreditRequest request);

  @Override
  @Mapping(source = "producer", target = "producer")
  BeatCreditResponse toDto(BeatCredit beatCredit);

  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "validationDate", ignore = true)
  @Mapping(target = "beat", ignore = true)
  @Mapping(target = "producer", ignore = true)
  @Mapping(target = "status", ignore = true)
  void updateEntityFromDto(UpdateBeatCreditRequest request, @MappingTarget BeatCredit beatCredit);
}
