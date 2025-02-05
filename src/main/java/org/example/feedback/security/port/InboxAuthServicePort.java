package org.example.feedback.security.port;

public interface InboxAuthServicePort {

    void verifyInboxOwnership(final String username, final String signature);
}

