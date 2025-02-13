package org.example.feedback.domain.service.adapter;

import lombok.RequiredArgsConstructor;
import org.example.feedback.security.adapter.OwnershipAuthServiceImpl;
import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.MessageResponse;
import org.example.feedback.mapper.message.MessageMapper;
import org.example.feedback.domain.model.Inbox;
import org.example.feedback.domain.model.Message;
import org.example.feedback.domain.repository.InboxRepository;
import org.example.feedback.domain.repository.MessageRepository;
import org.example.feedback.domain.service.port.MessageService;
import org.example.feedback.security.SignatureService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final OwnershipAuthServiceImpl ownershipAuthService;
    private final SignatureService signatureService;
    private final MessageRepository messageRepository;
    private final InboxRepository inboxRepository;

    public MessageResponse create(UUID inboxId, MessageRequest messageRequest, String username, String secret) {

        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new IllegalArgumentException("Inbox not found"));

        if (inbox.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Inbox has expired!");
        }

        String signature = determineSignature(username, secret);

        validateAnonymity(signature, inbox);

        Message message = Message.builder()
                .body(messageRequest.getBody())
                .signature(signature)
                .inbox(inbox)
                .build();

        messageRepository.save(message);

        return MessageMapper.INSTANCE.messageToMessageResponse(message);
    }

    public List<MessageResponse> getByInbox(UUID inboxId, String username, String secret) {
        Inbox inbox = inboxRepository.findById(inboxId)
                .orElseThrow(() -> new IllegalArgumentException("Inbox not found"));

        ownershipAuthService.verifyInboxOwnership(username, secret, inbox.getSignature());

        return MessageMapper.INSTANCE.messagesToResponse(messageRepository.findByInboxId(inboxId));
    }

    private String determineSignature(String username, String secret) {
        if (username != null && !username.isBlank() && secret!= null && !secret.isBlank()) {
            return signatureService.generateSignature(username, secret);
        }
        return null;
    }

    private void validateAnonymity(String signature, Inbox inbox) {
        if (signature == null && !inbox.isAllowAnonymous()) {
            throw new IllegalArgumentException("Anonymous messages are not allowed in this inbox");
        }
    }
}

