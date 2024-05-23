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

    public static String getDescriptionForType(String typeName) {
        String lowercaseTypeName = typeName.toLowerCase();
        if (HOUSE.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return "Casa";
        } else if (APARTMENT.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return "Departamento";
        } else if (CABIN.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return "Cabaña";
        } else if (CAMP.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return "Campamento";
        } else if (CAMPER.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return "Casa rodante";
        } else if (SHIP.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return "Barco";
        } else {
            return null;
        }
    }
}
