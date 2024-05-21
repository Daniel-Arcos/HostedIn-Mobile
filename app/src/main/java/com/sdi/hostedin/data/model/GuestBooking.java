package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.Objects;

public class GuestBooking implements Parcelable {
    private String _id;
    @Json(name="accommodationId")
    private Accommodation accommodation;
    private String beginningDate;
    private String endingDate;
    private int numberOfGuests;
    private double totalCost;
    private String bookingStatus;
    private String guestUser;
    private String hostUser;

    public GuestBooking() {
    }

    public GuestBooking(String _id, Accommodation accommodation, String beginningDate, String endingDate, int numberOfGuests, double totalCost, String bookingStatus, String guestUser, String hostUser) {
        this._id = _id;
        this.accommodation = accommodation;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.numberOfGuests = numberOfGuests;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
        this.guestUser = guestUser;
        this.hostUser = hostUser;
    }

    protected GuestBooking(Parcel in) {
        _id = in.readString();
        accommodation = in.readParcelable(Accommodation.class.getClassLoader());
        beginningDate = in.readString();
        endingDate = in.readString();
        numberOfGuests = in.readInt();
        totalCost = in.readDouble();
        bookingStatus = in.readString();
        guestUser = in.readString();
        hostUser = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeParcelable(accommodation, flags);
        dest.writeString(beginningDate);
        dest.writeString(endingDate);
        dest.writeInt(numberOfGuests);
        dest.writeDouble(totalCost);
        dest.writeString(bookingStatus);
        dest.writeString(guestUser);
        dest.writeString(hostUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuestBooking> CREATOR = new Creator<GuestBooking>() {
        @Override
        public GuestBooking createFromParcel(Parcel in) {
            return new GuestBooking(in);
        }

        @Override
        public GuestBooking[] newArray(int size) {
            return new GuestBooking[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(String beginningDate) {
        this.beginningDate = beginningDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getGuestUser() {
        return guestUser;
    }

    public void setGuestUser(String guestUser) {
        this.guestUser = guestUser;
    }

    public String getHostUser() {
        return hostUser;
    }

    public void setHostUser(String hostUser) {
        this.hostUser = hostUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuestBooking that = (GuestBooking) o;
        return numberOfGuests == that.numberOfGuests &&
                Double.compare(that.totalCost, totalCost) == 0 &&
                Objects.equals(_id, that._id) &&
                Objects.equals(accommodation, that.accommodation) &&
                Objects.equals(beginningDate, that.beginningDate) &&
                Objects.equals(endingDate, that.endingDate) &&
                Objects.equals(bookingStatus, that.bookingStatus) &&
                Objects.equals(guestUser,that.guestUser) &&
                Objects.equals(hostUser, that.hostUser);
    }
}
