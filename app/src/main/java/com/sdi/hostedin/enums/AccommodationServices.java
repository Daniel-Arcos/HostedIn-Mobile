package com.sdi.hostedin.enums;

public enum AccommodationServices {
    INTERNET("internet"),
    TV("tv"),
    KITCHEN("kitchen"),
    WASHING_MACHINE("washing machine"),
    PARKING("parking"),
    AIR_CONDITIONING("air conditioning"),
    POOL("pool"),
    GARDEN("garden"),
    LIGHT("light"),
    WATER("water");

    private final String description;

    AccommodationServices(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
