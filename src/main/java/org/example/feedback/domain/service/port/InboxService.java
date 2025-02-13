package org.example.feedback.domain.service.port;


import org.example.feedback.dto.request.InboxRequest;
import org.example.feedback.dto.response.InboxResponse;

import java.util.List;

public interface InboxService {

    InboxResponse createInbox(InboxRequest inboxRequest, String username, String secret);
    List<InboxResponse> getAllInboxes();
}

