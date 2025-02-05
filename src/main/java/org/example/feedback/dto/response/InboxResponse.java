package org.example.feedback.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class InboxResponse {

    UUID id;
    String topic;
    LocalDateTime expirationDate;
    boolean allowAnonymous;
}
