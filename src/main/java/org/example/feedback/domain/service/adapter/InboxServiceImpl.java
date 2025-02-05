package org.example.feedback.domain.service.adapter;

import lombok.RequiredArgsConstructor;
import org.example.feedback.dto.request.InboxRequest;
import org.example.feedback.dto.response.InboxResponse;
import org.example.feedback.mapper.index.InboxMapper;
import org.example.feedback.domain.model.Inbox;
import org.example.feedback.domain.repository.InboxRepository;
import org.example.feedback.security.SignatureService;
import org.example.feedback.domain.service.port.InboxService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final SignatureService signatureService;
    private final InboxRepository inboxRepository;

    public InboxResponse createInbox(InboxRequest inboxRequest) {
        String signature = signatureService.generateSignature(inboxRequest.getUsername());

        Inbox inbox = Inbox.builder()
                .topic(inboxRequest.getTopic())
                .signature(signature)
                .expirationDate(LocalDateTime.now().plusDays(inboxRequest.getExpiresInDays()))
                .allowAnonymous(inboxRequest.isAllowAnonymous())
                .build();
        Inbox entity = inboxRepository.save(inbox);

        return InboxMapper.INSTANCE.inboxToInboxResponse(entity);
    }

    public List<InboxResponse> getAllInboxes() {
        return InboxMapper.INSTANCE.inboxesToResponse(inboxRepository.findAll());
    }
}
