package org.example.feedback.security;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class SignatureService {

    private static final String SECRET = "superSecretKey123";
    private static final String SALT = "randomSaltValue";
    private static final String SEPARATOR = "#";
    private static final String ALGORITHM = "SHA-256";

    public String generateSignature(final String username) {
        String toHash = username + SEPARATOR + SECRET + SALT;
        return username + SEPARATOR + hashString(toHash);
    }

    public boolean validateSignature(final String username, final String signature) {
        String expectedSignature = generateSignature(username);
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

