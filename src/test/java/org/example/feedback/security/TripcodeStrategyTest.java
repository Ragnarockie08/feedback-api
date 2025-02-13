package org.example.feedback.security;

import org.example.feedback.security.adapter.TripcodeStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripcodeStrategyTest {

    @Spy
    private TripcodeStrategy signatureService;

    @Test
    void testGenerateSignature() {
        String username = "testUser";
        String secret = "secret";
        String signature = signatureService.generateSignature(username, secret);
        assertNotNull(signature);
        assertTrue(signature.startsWith(username + "#"));
    }

    @Test
    void testValidateSignature_Valid() {
        String username = "testUser";
        String secret = "secret";
        String signature = signatureService.generateSignature(username, secret);
        assertTrue(signatureService.validateSignature(username, secret, signature));
    }

    @Test
    void testValidateSignature_Invalid() {
        String username = "testUser";
        String secret = "secrte";
        String wrongSignature = "wrongSignature";
        assertFalse(signatureService.validateSignature(username, secret, wrongSignature));
    }

    @Test
    void testHashStringConsistency() {
        String input = "sampleInput";
        String secret = "secret";
        String hash1 = signatureService.generateSignature(input, secret);
        String hash2 = signatureService.generateSignature(input, secret);
        assertEquals(hash1, hash2);
    }
}