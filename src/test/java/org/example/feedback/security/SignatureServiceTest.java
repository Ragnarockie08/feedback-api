package org.example.feedback.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SignatureServiceTest {

    @Spy
    private SignatureService signatureService;

    @Test
    void testGenerateSignature() {
        String username = "testUser";
        String signature = signatureService.generateSignature(username);
        assertNotNull(signature);
        assertTrue(signature.startsWith(username + "#"));
    }

    @Test
    void testValidateSignature_Valid() {
        String username = "testUser";
        String signature = signatureService.generateSignature(username);
        assertTrue(signatureService.validateSignature(username, signature));
    }

    @Test
    void testValidateSignature_Invalid() {
        String username = "testUser";
        String wrongSignature = "wrongSignature";
        assertFalse(signatureService.validateSignature(username, wrongSignature));
    }

    @Test
    void testHashStringConsistency() {
        String input = "sampleInput";
        String hash1 = signatureService.generateSignature(input);
        String hash2 = signatureService.generateSignature(input);
        assertEquals(hash1, hash2);
    }
}