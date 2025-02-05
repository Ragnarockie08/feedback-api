package org.example.feedback.controller;

import org.example.feedback.config.SecurityConfig;
import org.example.feedback.domain.service.port.InboxService;
import org.example.feedback.domain.service.port.MessageService;
import org.example.feedback.dto.request.InboxRequest;
import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.InboxResponse;
import org.example.feedback.dto.response.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(InboxController.class)
public class InboxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InboxService inboxService;

    @MockitoBean
    private MessageService messageService;

    @Test
    void shouldCreateInboxSuccessfully() throws Exception {
        //given
        String requestBody = """
            {
                "topic": "Test Topic",
                "username": "user123",
                "expiresInDays": 7,
                "allowAnonymous": true
            }
        """;

        //when
        InboxResponse inboxResponse = InboxResponse.builder()
                .id(UUID.randomUUID())
                .topic("Test Topic")
                .expirationDate(LocalDateTime.now().plusDays(5))
                .allowAnonymous(true)
                .build();

        when(inboxService.createInbox(any(InboxRequest.class))).thenReturn(inboxResponse);

        //then
        mockMvc.perform(post("/api/inboxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("Test Topic"));
    }

    @Test
    void shouldRetrieveAllInboxesSuccessfully() throws Exception {
        //given
        InboxResponse inboxResponse = InboxResponse.builder()
                .id(UUID.randomUUID())
                .topic("Test Topic")
                .expirationDate(LocalDateTime.now().plusDays(5))
                .allowAnonymous(true)
                .build();        when(inboxService.getAllInboxes()).thenReturn(List.of(inboxResponse));

        //when & then
        mockMvc.perform(get("/api/inboxes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldPostMessageSuccessfully() throws Exception {
        String requestBody = """
            {
                "body": "Test message",
                "username": "user123"
            }
        """;

        MessageResponse messageResponse = MessageResponse.builder()
                .id(UUID.randomUUID())
                .body("Test message")
                .timestamp(LocalDateTime.now())
                .build();
        when(messageService.postMessage(any(UUID.class), any(MessageRequest.class))).thenReturn(messageResponse);

        mockMvc.perform(post("/api/inboxes/{inboxId}/messages", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("Test message"));
    }

    @Test
    void shouldReturnUnauthorizedWhenGettingMessagesWithInvalidUser() throws Exception {
        UUID inboxId = UUID.randomUUID();
        doThrow(new SecurityException("Invalid credentials"))
                .when(messageService).getMessagesByInbox(any(UUID.class), eq("wrongUser"));

        mockMvc.perform(get("/api/inboxes/{inboxId}/messages", inboxId)
                        .header("x-user-name", "wrongUser"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInboxWithInvalidData() throws Exception {
        String requestBody = """
            {
                "topic": "",
                "username": "",
                "expiresInDays": 0,
                "allowAnonymous": true
            }
        """;

        mockMvc.perform(post("/api/inboxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field == 'topic')].message").value("Topic must not be blank"))
                .andExpect(jsonPath("$[?(@.field == 'username')].message").value("Username must not be blank"))
                .andExpect(jsonPath("$[?(@.field == 'expiresInDays')].message").value("ExpiresIn number must be greater than 0"));
    }
}
