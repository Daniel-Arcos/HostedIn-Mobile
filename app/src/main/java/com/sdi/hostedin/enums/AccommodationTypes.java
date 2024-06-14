package com.sdi.hostedin.enums;

import android.content.Context;

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

    public static String getDescriptionForType(Context context, String typeName) {
        String lowercaseTypeName = typeName.toLowerCase();
        if (HOUSE.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return context.getString(R.string.house);
        } else if (APARTMENT.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return context.getString(R.string.apartment);
        } else if (CABIN.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return context.getString(R.string.cabin);
        } else if (CAMP.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return context.getString(R.string.camp);
        } else if (CAMPER.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return context.getString(R.string.camper);
        } else if (SHIP.getDescription().equalsIgnoreCase(lowercaseTypeName)) {
            return context.getString(R.string.ship);
        } else {
            return null;
        }
    }


}
