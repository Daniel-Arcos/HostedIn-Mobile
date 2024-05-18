package com.sdi.hostedin.data.model;

import java.util.Arrays;

public class Accommodation {
    private String _id;
    private String accommodationType;
    private double nightPrice;
    private int guestsNumber;
    private int roomsNumber;
    private int bedsNumber;
    private int bathroomsNumber;
    private String[] accommodationServices;
    private String rules;
    private Location location;

    public Accommodation() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String[] getAccommodationServices() {
        return accommodationServices;
    }

    public void setAccommodationServices(String[] accommodationServices) {
        this.accommodationServices = accommodationServices;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "_id='" + _id + '\'' +
                ", nightPrice=" + nightPrice +
                ", guestsNumber=" + guestsNumber +
                ", roomsNumber=" + roomsNumber +
                ", bedsNumber=" + bedsNumber +
                ", bathroomsNumber=" + bathroomsNumber +
                ", rules='" + rules + '\'' +
                ", accommodationType='" + accommodationType + '\'' +
                ", location=" + (location != null ? location.toString() : "null") +
                ", accommodationServices=" + (accommodationServices != null ? String.join(", ", accommodationServices) : "null") +
                '}';
    }
}
