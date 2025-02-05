package org.example.feedback.security;


import lombok.RequiredArgsConstructor;
import org.example.feedback.security.port.InboxAuthServicePort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InboxAuthService implements InboxAuthServicePort {

    private final SignatureService signatureService;

    public void verifyInboxOwnership(final String username, final String signature) {
        if (!signatureService.validateSignature(username, signature)) {
            throw new SecurityException("Invalid credentials: User does not own this inbox");
        }
    }
}

