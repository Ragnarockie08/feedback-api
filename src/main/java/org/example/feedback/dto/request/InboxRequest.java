package org.example.feedback.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class InboxRequest {

    @NotBlank(message = "Topic must not be blank")
    String topic;
    @Min(value = 1, message = "ExpiresIn number must be greater than 0")
    int expiresInDays;
    boolean allowAnonymous;
}
