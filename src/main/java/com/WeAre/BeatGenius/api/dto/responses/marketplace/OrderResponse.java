package com.WeAre.BeatGenius.api.dto.responses.marketplace;

import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.api.dto.responses.user.UserResponse;
import com.WeAre.BeatGenius.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderResponse {
  private Long id;
  private UserResponse buyer;
  private BeatResponse beat;
  private LicenseResponse license; // Ajout de la licence achetée
  private BigDecimal totalPrice;
  private OrderStatus status;
  private LocalDateTime createdAt;
}
