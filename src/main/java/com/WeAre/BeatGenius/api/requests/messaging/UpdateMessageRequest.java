package com.WeAre.BeatGenius.api.requests.messaging;

import lombok.Data;

@Data
public class UpdateMessageRequest {
    private String content;
    private boolean read;
    // On ne permet pas de modifier sender/recipient/sentAt pour des raisons de sécurité et d'intégrité
}