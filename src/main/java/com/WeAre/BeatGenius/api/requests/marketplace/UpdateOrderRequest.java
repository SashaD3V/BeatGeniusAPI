package com.WeAre.BeatGenius.api.requests.marketplace;

import com.WeAre.BeatGenius.domain.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private OrderStatus status;
    // On permet uniquement de modifier le status de la commande
    // Les autres champs ne devraient pas être modifiables une fois la commande créée
}