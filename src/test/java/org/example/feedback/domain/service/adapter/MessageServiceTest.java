package org.example.feedback.domain.service.adapter;

import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.MessageResponse;
import org.example.feedback.domain.model.Inbox;
import org.example.feedback.domain.model.Message;
import org.example.feedback.domain.repository.InboxRepository;
import org.example.feedback.domain.repository.MessageRepository;
import org.example.feedback.security.InboxAuthService;
import org.example.feedback.security.SignatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private SignatureService signatureService;
    @Mock
    private InboxAuthService inboxAuthService;
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private InboxRepository inboxRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private UUID inboxId;
    private Inbox inbox;
    private MessageRequest messageRequest;

    @BeforeEach
    void setUp() {
        inboxId = UUID.randomUUID();
        inbox = new Inbox(inboxId, "Test Topic", "user123#signature", LocalDateTime.now().plusDays(1), true, List.of());
        messageRequest = new MessageRequest("Test message", "user123");
    }

    @Test
    void shouldPostMessageSuccessfully() {
        // Given
        when(inboxRepository.findById(inboxId)).thenReturn(Optional.of(inbox));
        when(signatureService.generateSignature("user123")).thenReturn("user123#signature");

        // When
        MessageResponse response = messageService.postMessage(inboxId, messageRequest);

        // Then
        assertNotNull(response);
        assertEquals("Test message", response.getBody());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void shouldThrowExceptionWhenInboxNotFound() {
        // Given
        when(inboxRepository.findById(inboxId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> messageService.postMessage(inboxId, messageRequest));
    }

    @Test
    void shouldThrowExceptionWhenInboxExpired() {
        // Given
        inbox.setExpirationDate(LocalDateTime.now().minusDays(1));
        when(inboxRepository.findById(inboxId)).thenReturn(Optional.of(inbox));

        // When & Then
        assertThrows(IllegalStateException.class, () -> messageService.postMessage(inboxId, messageRequest));
    }

    @Test
    void shouldRetrieveInboxMessagesSuccessfully() {
        // Given
        when(inboxRepository.findById(inboxId)).thenReturn(Optional.of(inbox));
        doNothing().when(inboxAuthService).verifyInboxOwnership("user123", inbox.getSignature());
        when(messageRepository.findByInboxId(inboxId)).thenReturn(List.of(new Message()));

        // When
        List<MessageResponse> responses = messageService.getMessagesByInbox(inboxId, "user123");

        // Then
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenInvalidUserTriesToAccessMessages() {
        // Given
        when(inboxRepository.findById(inboxId)).thenReturn(Optional.of(inbox));
        doThrow(new SecurityException("Invalid credentials"))
                .when(inboxAuthService)
                .verifyInboxOwnership("wrongUser", "user123#signature");

        // When & Then
        assertThrows(SecurityException.class, () -> messageService.getMessagesByInbox(inboxId, "wrongUser"));
    }
}
