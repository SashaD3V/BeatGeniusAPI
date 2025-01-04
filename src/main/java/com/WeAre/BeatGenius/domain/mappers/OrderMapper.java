package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.dto.requests.marketplace.CreateOrderRequest;
import com.WeAre.BeatGenius.api.dto.responses.marketplace.OrderResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {BeatMapper.class, UserMapper.class, LicenseMapper.class})
public interface OrderMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "beat", source = "beat")
  @Mapping(target = "buyer", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "license", ignore = true) // Sera défini dans le service
  @Mapping(target = "totalPrice", ignore = true) // Sera calculé dans le service
  Order toEntity(CreateOrderRequest request, Beat beat);

  @Mapping(source = "buyer", target = "buyer")
  @Mapping(source = "beat", target = "beat")
  @Mapping(source = "license", target = "license")
  @Mapping(source = "totalPrice", target = "totalPrice")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "createdAt", target = "createdAt")
  OrderResponse toResponse(Order order);
}
