package org.example.feedback.domain.service.port;

import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse create(UUID inboxId, MessageRequest messageRequest, String username, String secret);
    List<MessageResponse> getByInbox(UUID inboxId, String username, String secret);
}
