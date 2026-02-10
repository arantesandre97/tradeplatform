package org.mypersonalprojects.tradeplatform.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    void shouldCreateAnAccount() {
        Account account = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        Assertions.assertEquals("John Doe", account.getName());
        Assertions.assertEquals("john.doe@example.com", account.getEmail());
        Assertions.assertEquals("62573679055", account.getDocument());
        Assertions.assertNotEquals("Password@123", account.getPassword());
        Assertions.assertNotNull(account.getPassword());
    }

    @Test
    void shouldCreateAnAccountWithADocumentWithSpecialCaracters() {
        Account account = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "625.736.790-55", 
            "Password@123"
        );

        Assertions.assertEquals("John Doe", account.getName());
        Assertions.assertEquals("john.doe@example.com", account.getEmail());
        Assertions.assertEquals("62573679055", account.getDocument());
        Assertions.assertNotEquals("Password@123", account.getPassword());
        Assertions.assertNotNull(account.getPassword());
    }

    @Test
    void shouldNotCreateAnAccountWithoutLastName() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    void shouldNotCreateAnAccountWithNameContainingSpecialCaractersAndNumbers() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John @123", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    void shouldNotCreateAnAccountWithInvalidEmail() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe.example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    void shouldNotCreateAnAccountWithInvalidDocument() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe@example.com", 
            "123456789", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid document", exception.getMessage());
    }

    @Test
    void shouldNotCreateAnAccountWithInvalidPassword() {
    var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "password123"
            );
        });

        Assertions.assertEquals("Invalid password", exception.getMessage());
    }
}
