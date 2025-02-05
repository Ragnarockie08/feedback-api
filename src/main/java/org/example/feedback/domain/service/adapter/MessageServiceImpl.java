package org.example.feedback.domain.service.adapter;

import lombok.RequiredArgsConstructor;
import org.example.feedback.security.InboxAuthService;
import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.MessageResponse;
import org.example.feedback.mapper.message.MessageMapper;
import org.example.feedback.domain.model.Inbox;
import org.example.feedback.domain.model.Message;
import org.example.feedback.domain.repository.InboxRepository;
import org.example.feedback.domain.repository.MessageRepository;
import org.example.feedback.security.SignatureService;
import org.example.feedback.domain.service.port.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final InboxAuthService inboxAuthService;
    private final SignatureService signatureService;
    private final MessageRepository messageRepository;
    private final InboxRepository inboxRepository;

    public MessageResponse postMessage(UUID inboxId, MessageRequest messageRequest) {
        Optional<Inbox> inboxOpt = inboxRepository.findById(inboxId);
        if (inboxOpt.isEmpty()) {
            throw new IllegalArgumentException("Inbox not found");
        }
        Inbox inbox = inboxOpt.get();

        if (inbox.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Inbox has expired!");
        }

        String signature = determineSignature(inbox, messageRequest.getUsername());

        Message message = Message.builder()
                .body(messageRequest.getBody())
                .signature(signature)
                .inbox(inbox)
                .build();

        messageRepository.save(message);

        return MessageMapper.INSTANCE.messageToMessageResponse(message);
    }

    public List<MessageResponse> getMessagesByInbox(UUID inboxId, String username) {
        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new IllegalArgumentException("Inbox not found"));

        inboxAuthService.verifyInboxOwnership(username, inbox.getSignature());

        return MessageMapper.INSTANCE.messagesToResponse(messageRepository.findByInboxId(inboxId));
    }

    private String determineSignature(Inbox inbox, String username) {
        if (username != null && !username.isBlank()) {
            return signatureService.generateSignature(username);
        }
        if (inbox.isAllowAnonymous()) {
            return null;
        }
        throw new IllegalArgumentException("Anonymous messages are not allowed in this inbox");
    }
}

