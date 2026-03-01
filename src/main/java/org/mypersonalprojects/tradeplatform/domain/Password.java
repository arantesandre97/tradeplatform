package org.mypersonalprojects.tradeplatform.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Password {
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password create(String plainPassword) {
        if (plainPassword == null || !plainPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$")) {
            throw new IllegalArgumentException("Invalid password");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            String hashedPassword = Base64.getEncoder().encodeToString(encodedhash);
            return new Password(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static Password restore(String hashedPassword) {
        return new Password(hashedPassword);
    }

    public String getValue() {
        return value;
    }
}
