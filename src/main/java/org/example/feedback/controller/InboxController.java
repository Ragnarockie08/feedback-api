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
    public ResponseEntity<InboxResponse> createInbox(@Valid @RequestBody InboxRequest inboxRequest,
                                                     @RequestHeader("x-user-name") String username,
                                                     @RequestHeader("x-user-secret") String secret) {
        return ResponseEntity.ok(inboxService.createInbox(inboxRequest, username, secret));
    }

    @GetMapping
    public ResponseEntity<List<InboxResponse>> getAllInboxes() {
        List<InboxResponse> inboxes = inboxService.getAllInboxes();
        return inboxes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(inboxes);
    }

    @PostMapping("/{inboxId}/messages")
    public ResponseEntity<MessageResponse> postMessage(@PathVariable UUID inboxId,
                                                       @Valid @RequestBody MessageRequest messageRequest,
                                                       @RequestHeader(value = "x-user-name", required = false) String username,
                                                       @RequestHeader(value = "x-user-secret", required = false) String secret) {
        MessageResponse response = messageService.create(inboxId, messageRequest, username, secret);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{inboxId}/messages")
    public ResponseEntity<List<MessageResponse>> getInboxMessages(
            @PathVariable UUID inboxId,
            @RequestHeader("x-user-name") String username,
            @RequestHeader("x-user-secret") String secret) {
        List<MessageResponse> messages = messageService.getByInbox(inboxId, username, secret);
        return ResponseEntity.ok(messages);
    }
}
