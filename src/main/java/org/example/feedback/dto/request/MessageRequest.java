package org.example.feedback.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@Value
public class MessageRequest {

    @NotBlank
    String body;

    public MessageRequest(String body) {
        this.body = body;
    }
}
