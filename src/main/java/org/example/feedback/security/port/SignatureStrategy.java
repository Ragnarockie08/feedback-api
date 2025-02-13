package org.example.feedback.security.port;

public interface SignatureStrategy {

    String generateSignature(final String input, final String secret);
    boolean validateSignature(final String username, final String secret, final String signature);
}
