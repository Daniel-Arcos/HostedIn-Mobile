package com.sdi.hostedin.enums;

public enum BookingSatuses {

    CURRENT("Current"),
    OVERDUE("Overdue");

    private final String description;

    BookingSatuses(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
