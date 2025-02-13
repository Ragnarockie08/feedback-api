package org.example.feedback.security.adapter;

import org.example.feedback.security.port.SignatureStrategy;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class TripcodeStrategy implements SignatureStrategy {

    private static final String SALT = "randomSaltValue";
    private static final String SEPARATOR = "#";
    private static final String ALGORITHM = "SHA-256";

    public String generateSignature(final String username, final String secret) {
        String toHash = username + SEPARATOR + secret + SALT;
        return username + SEPARATOR + hashString(toHash);
    }

    public boolean validateSignature(final String username, final String secret, final String signature) {
        String expectedSignature = generateSignature(username, secret);
        return expectedSignature.equals(signature);
    }

    private String hashString(final String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}

