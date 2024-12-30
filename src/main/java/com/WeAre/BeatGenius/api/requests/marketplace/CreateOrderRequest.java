package com.WeAre.BeatGenius.api.requests.marketplace;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long beatId;  // ID du beat à acheter
    // On ne met pas buyerId car il sera récupéré depuis l'utilisateur authentifié
    // totalPrice sera calculé côté serveur
    // status sera initialisé côté serveur
}