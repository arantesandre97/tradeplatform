package org.mypersonalprojects.tradeplatform.domain;

public class Email {
    private final String value;

    public Email(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.value = email;
    }

    public String getValue() {
        return value;
    }
}
