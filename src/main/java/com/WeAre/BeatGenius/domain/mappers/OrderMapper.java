package com.WeAre.BeatGenius.domain.mappers;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateOrderRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateOrderRequest;
import com.WeAre.BeatGenius.api.responses.marketplace.OrderResponse;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "totalPrice", source = "beat.price")
    @Mapping(target = "beat", source = "beat")
    @Mapping(target = "buyer", ignore = true)  // On l'assignera dans le service
    @Mapping(target = "status", expression = "java(com.WeAre.BeatGenius.domain.enums.OrderStatus.PENDING)")  // Valeur par défaut
    Order toEntity(CreateOrderRequest request, Beat beat);

    OrderResponse toResponse(Order order);
}