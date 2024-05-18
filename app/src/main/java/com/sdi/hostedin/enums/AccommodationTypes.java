package com.sdi.hostedin.enums;

import com.sdi.hostedin.R;

public enum AccommodationTypes {
    HOUSE("house"),
    APARTMENT("apartment"),
    CABIN("cabin"),
    CAMP("camp"),
    CAMPER("camper"),
    SHIP("ship");

    private final String description;

    AccommodationTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
