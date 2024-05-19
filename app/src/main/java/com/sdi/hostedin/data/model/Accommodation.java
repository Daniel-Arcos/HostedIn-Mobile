package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Accommodation implements Parcelable {
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

    protected Accommodation(Parcel in) {
        _id = in.readString();
        title = in.readString();
        description = in.readString();
        rules = in.readString();
        accommodationType = in.readString();
        nightPrice = in.readDouble();
        guestsNumber = in.readInt();
        roomsNumber = in.readInt();
        bedsNumber = in.readInt();
        bathroomsNumber = in.readInt();
        accommodationServices = in.createStringArray();
        location = in.readParcelable(Location.class.getClassLoader());
        userId = in.readString();
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(rules);
        dest.writeString(accommodationType);
        dest.writeDouble(nightPrice);
        dest.writeInt(guestsNumber);
        dest.writeInt(roomsNumber);
        dest.writeInt(bedsNumber);
        dest.writeInt(bathroomsNumber);
        dest.writeStringArray(accommodationServices);
        dest.writeParcelable(location, flags);
        dest.writeString(userId);
    }
}
