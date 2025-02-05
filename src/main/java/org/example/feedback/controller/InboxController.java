package org.example.feedback.controller;

import jakarta.validation.Valid;
import org.example.feedback.domain.service.port.InboxService;
import org.example.feedback.domain.service.port.MessageService;
import org.example.feedback.dto.request.InboxRequest;
import org.example.feedback.dto.request.MessageRequest;
import org.example.feedback.dto.response.InboxResponse;
import org.example.feedback.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inboxes")
public class InboxController {

    private final InboxService inboxService;
    private final MessageService messageService;

    public InboxController(InboxService inboxService, MessageService messageService) {
        this.inboxService = inboxService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<InboxResponse> createInbox(@Valid @RequestBody InboxRequest inboxRequest) {
        return ResponseEntity.ok(inboxService.createInbox(inboxRequest));
    }

    @GetMapping
    public ResponseEntity<List<InboxResponse>> getAllInboxes() {
        List<InboxResponse> inboxes = inboxService.getAllInboxes();
        return inboxes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(inboxes);
    }

    @PostMapping("/{inboxId}/messages")
    public ResponseEntity<MessageResponse> postMessage(@PathVariable UUID inboxId,
                                                       @Valid @RequestBody MessageRequest messageRequest) {
        MessageResponse response = messageService.postMessage(inboxId, messageRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{inboxId}/messages")
    public ResponseEntity<List<MessageResponse>> getInboxMessages(
            @PathVariable UUID inboxId,
            @RequestHeader("x-user-name") String username) {
        List<MessageResponse> messages = messageService.getMessagesByInbox(inboxId, username);
        return ResponseEntity.ok(messages);
    }
}
