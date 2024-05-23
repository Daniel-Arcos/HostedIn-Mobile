package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Booking implements Parcelable {

    private String _id;
    private Accommodation accommodation;
    private String beginningDate;
    private String endingDate;
    private int numberOfGuests;
    private double totalCost;
    private String bookingStatus;
    private User guestUser;
    private User hostUser;

    public Booking() {
    }

    public Booking(String _id, Accommodation accommodationId, String beginningDate, String endingDate, int numberOfGuest, double totalCost, String bookingStatus,
                   User guestUser, User hostUser) {
        this.accommodation = accommodationId;
        this._id = _id;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.numberOfGuests = numberOfGuest;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
        this.guestUser = guestUser;
        this.hostUser = hostUser;
    }


    protected Booking(Parcel in) {
        _id = in.readString();
        accommodation = in.readParcelable(Accommodation.class.getClassLoader());
        beginningDate = in.readString();
        endingDate = in.readString();
        numberOfGuests = in.readInt();
        totalCost = in.readDouble();
        bookingStatus = in.readString();
        //guestUser = (User)in.readParcelable(User.class.getClassLoader());
        //hostUser = (User)in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodationId) {
        this.accommodation = accommodationId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
    public User getGuestUser() {
        return guestUser;
    }

    public void setGuestUser(User guestUser) {
        this.guestUser = guestUser;
    }

    public User getHostUser() {
        return hostUser;
    }

    public void setHostUser(User hostUser) {
        this.hostUser = hostUser;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeParcelable(accommodation, flags);
        dest.writeString(beginningDate);
        dest.writeString(endingDate);
        dest.writeInt(numberOfGuests);
        dest.writeDouble(totalCost);
        dest.writeString(bookingStatus);
        dest.writeParcelable(guestUser, flags);
        dest.writeParcelable(hostUser, flags);
    }
}

