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

    public static String getDescriptionForService(String serviceName) {
        String lowercaseServiceName = serviceName.toLowerCase();
        if (INTERNET.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Internet";
        } else if (TV.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "TV";
        } else if (KITCHEN.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Cocina";
        } else if (WASHING_MACHINE.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Lavadora";
        } else if (PARKING.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Estacionamiento";
        } else if (AIR_CONDITIONING.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Aire acondicionado";
        } else if (POOL.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Alberca";
        } else if (GARDEN.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Jardín";
        } else if (LIGHT.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Luz";
        } else if (WATER.getDescription().equalsIgnoreCase(lowercaseServiceName)) {
            return "Agua";
        } else {
            return null;
        }
    }
}
