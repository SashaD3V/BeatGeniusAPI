package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateOrderRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateOrderRequest;
import com.WeAre.BeatGenius.api.responses.marketplace.OrderResponse;
import com.WeAre.BeatGenius.domain.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BeatMapper.class})
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Order toEntity(CreateOrderRequest request);

    void updateEntityFromDto(UpdateOrderRequest request, @MappingTarget Order order);

    OrderResponse toDto(Order order);
}