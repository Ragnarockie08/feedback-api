package org.example.feedback.security.adapter;


import lombok.RequiredArgsConstructor;
import org.example.feedback.security.SignatureService;
import org.example.feedback.security.port.OwnershipAuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnershipAuthServiceImpl implements OwnershipAuthService {

    private final SignatureService signatureService;

    public void verifyInboxOwnership(final String username, final String secret, final String signature) {
        if (!signatureService.validateSignature(username, secret, signature)) {
            throw new SecurityException("Invalid credentials: User does not own this inbox");
        }
    }
}

