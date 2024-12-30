package com.WeAre.BeatGenius.api.responses.marketplace;

import com.WeAre.BeatGenius.api.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.api.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private UserResponse buyer;
    private BeatResponse beat;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}