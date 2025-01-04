package com.WeAre.BeatGenius.api.dto.requests.messaging;

import lombok.Data;

@Data
public class CreateMessageRequest {
  private Long recipientId; // On utilise juste l'ID du destinataire
  private String content;
}