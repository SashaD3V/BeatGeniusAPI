package com.WeAre.BeatGenius.api.dto.requests.marketplace;

import lombok.Data;

@Data
public class CreateOrderRequest {
  private Long beatId;
  private Long licenseId; // ID de la licence choisie
}
