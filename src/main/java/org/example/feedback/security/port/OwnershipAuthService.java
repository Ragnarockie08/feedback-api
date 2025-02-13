package org.example.feedback.security.port;

public interface OwnershipAuthService {

    void verifyInboxOwnership(final String username, final String secret, final String signature);
}

