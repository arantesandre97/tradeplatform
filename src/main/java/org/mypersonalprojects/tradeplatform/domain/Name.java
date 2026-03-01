package org.mypersonalprojects.tradeplatform.domain;

public class Name {
    private final String value;

    public Name(String name) {
        if (name == null || name.isBlank() || !name.matches("[\\p{L} ]+") || name.split(" ").length < 2) {
            throw new IllegalArgumentException("Invalid name");
        }
        this.value = name;
    }

    public String getValue() {
        return value;
    }
}
