package org.example.feedback.security;

import org.example.feedback.security.port.SignatureStrategy;
import org.springframework.stereotype.Service;

@Service
public class SignatureService {
    private final SignatureStrategy signatureStrategy;

    public SignatureService(SignatureStrategy signatureStrategy) {
        this.signatureStrategy = signatureStrategy;
    }

    public String generateSignature(String input, String secret) {
        return signatureStrategy.generateSignature(input, secret);
    }

    public boolean validateSignature(String username, String secret, String signature) {
        return signatureStrategy.validateSignature(username, secret, signature);
    }
}