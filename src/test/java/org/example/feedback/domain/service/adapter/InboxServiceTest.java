package org.example.feedback.domain.service.adapter;

import org.example.feedback.domain.model.Inbox;
import org.example.feedback.domain.repository.InboxRepository;
import org.example.feedback.dto.request.InboxRequest;
import org.example.feedback.dto.response.InboxResponse;
import org.example.feedback.security.SignatureService;
import org.example.feedback.security.adapter.TripcodeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InboxServiceTest {

    @Mock
    private SignatureService signatureService;

    @Mock
    private InboxRepository inboxRepository;

    @InjectMocks
    private InboxServiceImpl inboxService;

    private InboxRequest inboxRequest;
    private Inbox inbox;

    @BeforeEach
    void setUp() {
        UUID inboxId = UUID.randomUUID();
        inboxRequest = new InboxRequest("Test Topic", 7, true);
        inbox = Inbox.builder()
                .id(inboxId)
                .topic("Test Topic")
                .signature("user123#signature")
                .expirationDate(LocalDateTime.now().plusDays(7))
                .allowAnonymous(true)
                .build();
    }

    @Test
    void shouldCreateInboxSuccessfully() {
        // Given
        when(signatureService.generateSignature("user123", "secret")).thenReturn("user123#signature");
        when(inboxRepository.save(any(Inbox.class))).thenReturn(inbox);

        // When
        InboxResponse response = inboxService.createInbox(inboxRequest, "user123", "secret");

        // Then
        assertNotNull(response);
        assertEquals("Test Topic", response.getTopic());
        verify(inboxRepository, times(1)).save(any(Inbox.class));
    }

    @Test
    void shouldRetrieveAllInboxesSuccessfully() {
        // Given
        when(inboxRepository.findAll()).thenReturn(List.of(inbox));

        // When
        List<InboxResponse> responses = inboxService.getAllInboxes();

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }
}
