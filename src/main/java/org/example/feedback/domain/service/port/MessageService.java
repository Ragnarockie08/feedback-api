package org.example.feedback.domain.service.port;

import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse postMessage(UUID inboxId, MessageRequest messageRequest);
    List<MessageResponse> getMessagesByInbox(UUID inboxId, String username);
}
