package org.mypersonalprojects.tradeplatform.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mypersonalprojects.tradeplatform.utils.ValidateCpf;

public class Account {
    private UUID id;
    private String name;
    private String email;
    private String document;
    private String password;

    @JsonCreator
    public Account(@JsonProperty("name") String name, 
                   @JsonProperty("email") String email, 
                   @JsonProperty("document") String document, 
                   @JsonProperty("password") String password) {
        this.id = UUID.randomUUID();
        this.name = isValidName(name);
        this.email = isValidEmail(email);
        this.document = isValidDocument(document);
        this.password = isValidPassword(password);
    }

    private Account(UUID id, String name, String email, String document, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
    }

    public static Account restore(UUID id, String name, String email, String document, String password) {
        return new Account(id, name, email, document, password);
    }

    private String isValidName(String name) {
        if (name.isBlank() || !name.matches("[\\p{L} ]+") || name.split(" ").length < 2) {
            throw new IllegalArgumentException("Invalid name");
        }

        return name;
    }

    private String isValidEmail(String email) {
        if (email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        return email;
    }

    private String isValidDocument(String document) {
        if (!ValidateCpf.isValid(document)) {
            throw new IllegalArgumentException("Invalid document");
        }

        return document.replaceAll("\\D", "");
    }

    private String isValidPassword(String password) {
        if (password == null || !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$")) {
            throw new IllegalArgumentException("Invalid password");    
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return document;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }
}
