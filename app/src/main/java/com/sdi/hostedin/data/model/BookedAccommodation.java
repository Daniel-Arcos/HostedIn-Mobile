package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Objects;

public class BookedAccommodation implements Parcelable {
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
    private byte[] mainImage;
    private String user;

    public BookedAccommodation() {

    }

    public BookedAccommodation(String _id, String title, String description, String rules, String accommodationType, double nightPrice, int guestsNumber, int roomsNumber, int bedsNumber, int bathroomsNumber, String[] accommodationServices, Location location, String user) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.rules = rules;
        this.accommodationType = accommodationType;
        this.nightPrice = nightPrice;
        this.guestsNumber = guestsNumber;
        this.roomsNumber = roomsNumber;
        this.bedsNumber = bedsNumber;
        this.bathroomsNumber = bathroomsNumber;
        this.accommodationServices = accommodationServices;
        this.location = location;
        this.user = user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public byte[] getMainImage() {
        return mainImage;
    }

    public void setMainImage(byte[] mainImage) {
        this.mainImage = mainImage;
    }

    protected BookedAccommodation(Parcel in) {
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
        user = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
        dest.writeString(user);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookedAccommodation> CREATOR = new Creator<BookedAccommodation>() {
        @Override
        public BookedAccommodation createFromParcel(Parcel in) {
            return new BookedAccommodation(in);
        }

        @Override
        public BookedAccommodation[] newArray(int size) {
            return new BookedAccommodation[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookedAccommodation that = (BookedAccommodation) o;
        return Double.compare(that.nightPrice, nightPrice) == 0 &&
                guestsNumber == that.guestsNumber &&
                roomsNumber == that.roomsNumber &&
                bedsNumber == that.bedsNumber &&
                bathroomsNumber == that.bathroomsNumber &&
                Objects.equals(_id, that._id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(rules, that.rules) &&
                Objects.equals(accommodationType, that.accommodationType) &&
                Arrays.equals(accommodationServices, that.accommodationServices) &&
                Objects.equals(location, that.location) &&
                Arrays.equals(mainImage, that.mainImage) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_id, title, description, rules, accommodationType, nightPrice, guestsNumber, roomsNumber, bedsNumber, bathroomsNumber, location, user);
        result = 31 * result + Arrays.hashCode(accommodationServices);
        result = 31 * result + Arrays.hashCode(mainImage);
        return result;
    }

}
