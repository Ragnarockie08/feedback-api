package org.example.feedback.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@Value
public class MessageRequest {

    @NotBlank
    String body;
    String username;

    public MessageRequest(String body, String username) {
        this.body = body;
        this.username = username;
    }
}
