package com.sdi.hostedin.data.model;

import java.util.Arrays;

public class Accommodation {
    private String _id;
    private String title;
    private String description;
    private String rules;
    private String accommodationType;
    private double nightPrice;
    private int guestsNumber;
    private int roomsNumber;
    private int bedsNumber;
    private int bathroomsNumber;
    private String[] accommodationServices;
    private Location location;
    private String userId;

    public Accommodation() {
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(String accommodationType) {
        this.accommodationType = accommodationType;
    }

    public double getNightPrice() {
        return nightPrice;
    }

    public void setNightPrice(double nightPrice) {
        this.nightPrice = nightPrice;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(int guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public int getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(int roomsNumber) {
        this.roomsNumber = roomsNumber;
    }

    public int getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(int bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public int getBathroomsNumber() {
        return bathroomsNumber;
    }

    public void setBathroomsNumber(int bathroomsNumber) {
        this.bathroomsNumber = bathroomsNumber;
    }

    public String[] getAccommodationServices() {
        return accommodationServices;
    }

    public void setAccommodationServices(String[] accommodationServices) {
        this.accommodationServices = accommodationServices;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "_id='" + _id + '\'' +
                ", title=" + title +
                ", description=" + description +
                ", nightPrice=" + nightPrice +
                ", guestsNumber=" + guestsNumber +
                ", roomsNumber=" + roomsNumber +
                ", bedsNumber=" + bedsNumber +
                ", bathroomsNumber=" + bathroomsNumber +
                ", rules='" + rules + '\'' +
                ", accommodationType='" + accommodationType + '\'' +
                ", location=" + (location != null ? location.toString() : "null") +
                ", accommodationServices=" + (accommodationServices != null ? String.join(", ", accommodationServices) : "null") +
                ", userId=" + userId +
                '}';
    }
}
