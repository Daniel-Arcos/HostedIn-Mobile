package com.sdi.hostedin.enums;

import android.content.Context;

import com.sdi.hostedin.R;

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

    public static String getDescriptionForService(Context context, String serviceName) {
        String lowercaseServiceName = serviceName.toLowerCase();
        if (INTERNET.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.internet);
        } else if (TV.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.tv);
        } else if (KITCHEN.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.kitchen);
        } else if (WASHING_MACHINE.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.washing_machine);
        } else if (PARKING.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.parking);
        } else if (AIR_CONDITIONING.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.air_conditioning);
        } else if (POOL.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.pool);
        } else if (GARDEN.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.garden);
        } else if (LIGHT.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.light);
        } else if (WATER.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return context.getString(R.string.water);
        } else {
            return "";
        }
    }
}
